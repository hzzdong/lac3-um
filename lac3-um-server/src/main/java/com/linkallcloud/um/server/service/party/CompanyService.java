package com.linkallcloud.um.server.service.party;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.activity.party.ICompanyActivity;
import com.linkallcloud.um.activity.party.IDepartmentActivity;
import com.linkallcloud.um.activity.party.IUserActivity;
import com.linkallcloud.um.activity.sys.IAccountActivity;
import com.linkallcloud.um.activity.sys.IApplicationActivity;
import com.linkallcloud.um.activity.sys.IAreaActivity;
import com.linkallcloud.um.constant.Consts;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.service.party.ICompanyService;

@Transactional(readOnly = true)
public abstract class CompanyService<C extends Company, CA extends ICompanyActivity<C>, U extends User, UA extends IUserActivity<U>, D extends Department, DA extends IDepartmentActivity<D>>
		extends OrgService<C, CA, U, UA> implements ICompanyService<C> {

	@Autowired
	protected IAccountActivity accountActivity;

	@Autowired
	private IAreaActivity areaActivity;

	@Autowired
	protected IApplicationActivity applicationActivity;

	public CompanyService() {
		super();
	}

	protected abstract DA getDepartmentActivity();

	@Override
	public List<C> findSubCompanies(Trace t, Long companyId) {
		return activity().findSubCompanies(t, companyId);
	}

	@Override
	public Long getCompanyAreaRootId(Trace t, Long companyId, Long appId) {
		return activity().getCompanyAreaRootId(t, companyId, appId);
	}

	@Override
	public Long[] getCompanyAppAreaRootIds(Trace t, Long companyId, Long appId) {
		return activity().getCompanyAppAreaRootIds(t, companyId, appId);
	}

	@Override
	public Long[] findPermedCompanyAppAreas(Trace t, Long companyId, Long appId) {
		return activity().findPermedCompanyAppAreas(t, companyId, appId);
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean saveCompanyAppAreaPerm(Trace t, Long companyId, String companyUuid, Long appId, String appUuid,
			Map<String, Long> areaUuidIds) {
		return activity().saveCompanyAppAreaPerm(t, companyId, companyUuid, appId, appUuid, areaUuidIds);
	}

	@Override
	public List<Tree> findCompanyValidOrgResource(Trace t, Long companyId) {
		return activity().findCompanyValidOrgResource(t, companyId);
	}

	@Override
	public List<C> findDirectCompaniesByParentId(Trace t, Long parentId) {
		return activity().findDirectCompaniesByParentId(t, parentId);
	}

	@Override
	public List<C> findAllCompaniesByParentCode(Trace t, String govCode) {
		return activity().findAllCompaniesByParentCode(t, govCode);
	}

	@Override
	public Tree getCompanyTree(Trace t, String treeType, Sid companyId) {
		return activity().getCompanyTree(t, treeType, companyId);
	}

	@Override
	public Tree getPermedCompanyTree(Trace t, Long appId, Long userId) {
		U user = getUserActivity().fetchById(t, userId);
		if (user == null) {
			throw new BaseRuntimeException("100001", "userId参数错误:" + userId);
		}

		C company = activity().fetchById(t, user.getCompanyId());
		if (user.getAccount().equals("superadmin") || user.isAdmin()) {
			return activity().getCompanyTree(t, Consts.ORG_TREE_TYPE_COMPANY, company.sid());
		} else {
			Tree root = getCompanyVirtulTreeNode(t, company);
			boolean depAdmin = getUserActivity().isUserDepartmentAdmin(t, userId);
			if (depAdmin) {
				if ("YwDepartment".equals(user.getParentClass()) || "KhDepartment".equals(user.getParentClass())) {
					Tree node = getDepartmentActivity().fetchById(t, user.getParentId()).toTreeNode();
					root.addChild(node);
				}
			} else {
				List<Long> permedOrgIds = getUserActivity().findUserAppPermedOrgs(t, userId, appId);
				if (permedOrgIds != null && !permedOrgIds.isEmpty()) {
					List<Tree> orgs = activity().getCompanyTreeList(t, Consts.ORG_TREE_TYPE_COMPANY, company.sid());
					List<Tree> items = new ArrayList<Tree>();
					for (Tree node : orgs) {
						for (Long orgId : permedOrgIds) {
							if (node.getId().equals(orgId.toString())) {
								items.add(node);
							}
						}
					}

					Trees.assembleTree(root, items);
					root.sort();
				}
			}
			return root;
		}
	}

	private Tree getCompanyVirtulTreeNode(Trace t, C company) {
		if (company == null) {
			throw new ArgException("companyId参数错误");
		}

		Tree root = company.toTreeNode();
		root.setType("v-root");
		root.setId("-" + company.getId());
		root.setpId(null);
		root.setOpen(true);
		return root;
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean saveCompanyAppMenuPerm(Trace t, Long id, String uuid, Long appId, String appUuid,
			Map<String, Long> menuUuidIds) {
		return activity().saveCompanyAppMenuPerm(t, id, uuid, appId, appUuid, menuUuidIds);
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean saveAppMenuPerm(Trace t, Sid companyId, Sid appId, Map<String, Long> menuUuidIds) {
		return activity().saveAppMenuPerm(t, companyId, appId, menuUuidIds);
	}

	@Override
	public Long[] getConfigCompanyAreaRootIds(Trace t, Sid companyId) {
		return activity().getConfigCompanyAreaRootIds(t, companyId);
	}

	@Override
	public List<NameValue> getConfigCompanyAreaRoots(Trace t, Sid companyId) {
		return activity().getConfigCompanyAreaRoots(t, companyId);
	}

	@Override
	public Long[] getCompanyAreaRootIds(Trace t, Sid companyId) {
		Long[] roots = getConfigCompanyAreaRootIds(t, companyId);
		if (roots != null && roots.length > 0) {
			return roots;
		} else {
			C company = activity().fetchByIdUuid(t, companyId.getId(), companyId.getUuid());
			if (company == null) {
				throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "company参数错误");
			}
			if (company.isTopParent()) {
				return null;
			} else {
				C parent = activity().fetchById(t, company.getParentId());
				return getCompanyAreaRootIds(t, parent.sid());
			}
		}
	}

	@Override
	public Tree loadCompanyAreaFullTree(Trace t, Sid companyId) {
		C company = activity().fetchByIdUuid(t, companyId.getId(), companyId.getUuid());
		if (company == null) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "company参数错误");
		}
		if (company.isTopParent()) {
			return areaActivity.getTree(t, true);
		} else {
			C parent = activity().fetchById(t, company.getParentId());
			return loadCompanyAreaTree(t, parent.sid());
		}
	}

	@Override
	public Tree loadCompanyAreaTree(Trace t, Sid companyId) {
		Long[] areaIds = getCompanyAreaRootIds(t, companyId);
		if (areaIds != null && areaIds.length > 0) {
			Tree root = Trees.vroot("区域");
			for (Long rootAreaId : areaIds) {
				Tree item = areaActivity.findChildrenTree(t, rootAreaId, new Equal("status", 0));
				root.addChild(item);
			}
			return root;
		} else {
			return areaActivity.getTree(t, true);
		}
	}

	@Override
	public List<Tree> findCompanyValidMenus(Trace t, Long companyId, Long appId) {
		return activity().findCompanyValidMenus(t, companyId, appId);
	}

	@Override
	public Long[] findPermedCompanyAppMenus(Trace t, Long companyId, Long appId) {
		return activity().findPermedCompanyAppMenus(t, companyId, appId);
	}

	@Override
	public Tree findPermedAppMenusTree(Trace t, Sid myCompanyId, Sid forCompanyId, Sid appId) {
		List<Tree> items = activity().findCompanyValidMenus(t, myCompanyId.getId(), appId.getId());
		Long[] permedMenuIds = activity().findPermedCompanyAppMenus(t, forCompanyId.getId(), appId.getId());
		if (items != null && permedMenuIds != null && permedMenuIds.length > 0) {
			CopyOnWriteArrayList<Long> pmids = new CopyOnWriteArrayList<Long>(permedMenuIds);
			checkMenus(t, items, pmids);
		}
		Tree root = Trees.assembleTree(items);
//		Tree vroot = Trees.vroot("菜单树");
//		Trees.assembleTree(vroot, items);
		root.sort();
		return root;
	}

	private void checkMenus(Trace t, List<Tree> items, CopyOnWriteArrayList<Long> permedMenuIds) {
		if (items == null || items.isEmpty() || permedMenuIds == null || permedMenuIds.isEmpty()) {
			return;
		}
		for (Tree item : items) {
			for (Long pid : permedMenuIds) {
				if (pid.toString().equals(item.getId())) {
					item.setChecked(true);
					permedMenuIds.remove(pid);
					break;
				}
			}
		}
	}

}
