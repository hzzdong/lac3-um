package com.linkallcloud.um.server.service.party;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public List<C> findDirectCompaniesByParentId(Trace t, Long parentId) {
		return activity().findDirectCompaniesByParentId(t, parentId);
	}

	@Override
	public List<C> findAllCompaniesByParentCode(Trace t, String govCode) {
		return activity().findAllCompaniesByParentCode(t, govCode);
	}

	@Override
	public Tree getCompanyTree(Trace t, String treeType, Sid companyId) {
		return activity().getCompanyTree(t, treeType, companyId.getId());
	}

	@Override
	public Tree getUserAppCompanyTree(Trace t, Long appId, Long userId, Long companyId) {
		U user = getUserActivity().fetchById(t, userId);
		if (user == null) {
			throw new BaseRuntimeException("100001", "userId参数错误:" + userId);
		}

		if(companyId == null) {
			companyId = user.getCompanyId();
		}
		C company = activity().fetchById(t, companyId);// user.getCompanyId()
		if (user.getAccount().equals("superadmin") || user.isAdmin()) {
			return activity().getCompanyTree(t, Consts.ORG_TREE_TYPE_COMPANY, company.getId());
		} else {
			boolean depAdmin = getUserActivity().isUserDepartmentAdmin(t, userId, companyId);
			if (depAdmin) {
				Tree vroot = getCompanyVirtulTreeNode(t, company);
				if ("YwDepartment".equals(user.getParentClass()) || "KhDepartment".equals(user.getParentClass())) {
					Tree node = getDepartmentActivity().fetchById(t, user.getParentId()).toTreeNode();
					vroot.addChild(node);
				}
				return vroot;
			} else {
				Tree root = null;
				String rootId = "-" + company.getId();
				List<Long> permedOrgIds = getUserActivity().getUserAppOrgs(t, companyId, userId, appId);
				if (permedOrgIds != null && !permedOrgIds.isEmpty()) {
					List<Tree> orgs = activity().getCompanyTreeList(t, Consts.ORG_TREE_TYPE_COMPANY, company.getId());
					List<Tree> items = new ArrayList<Tree>();
					for (Tree node : orgs) {
						for (Long orgId : permedOrgIds) {
							if (node.getId().equals(orgId.toString())) {
								if (node.getId().equals(rootId)) {
									root = node;
								} else {
									items.add(node);
								}
								break;
							}
						}
					}

					if (root == null) {
						root = getCompanyVirtulTreeNode(t, company);
					}
					Trees.assembleTree(root, items);
					root.sort();
				}
				return root;
			}

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
	public Boolean saveAppMenuPerm(Trace t, Sid companyId, Sid appId, Map<String, Long> menuUuidIds) {
		return activity().saveAppMenuPerm(t, companyId, appId, menuUuidIds);
	}

	@Override
	public Long[] getConfigCompanyAreaRootIds(Trace t, Long companyId) {
		return activity().getConfigCompanyAreaRootIds(t, companyId);
	}

	@Override
	public List<NameValue> getConfigCompanyAreaRoots(Trace t, Long companyId) {
		return activity().getConfigCompanyAreaRoots(t, companyId);
	}

	@Override
	public Long[] getCompanyAreaRootIds(Trace t, Long companyId) {
		return activity().getCompanyAreaRootIds(t, companyId);
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
		Long[] areaIds = getCompanyAreaRootIds(t, companyId.getId());
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
	public Tree loadCompanyOrgTree(Trace t, Long companyId) {
		return activity().loadCompanyOrgTree(t, companyId);
	}

	@Override
	public Tree loadCompanyMenuTree(Trace t, Long companyId, Long appId) {
		return activity().loadCompanyMenuTree(t, companyId, appId);
	}

	@Override
	public Tree findPermedAppMenusTree(Trace t, Sid myCompanyId, Sid forCompanyId, Sid appId) {
		Tree tree = activity().loadCompanyMenuTree(t, myCompanyId == null ? null : myCompanyId.getId(), appId.getId());
		Long[] permedItemIds = activity().findPermedCompanyAppMenus(t, forCompanyId.getId(), appId.getId());
		Trees.checked(tree, permedItemIds);
		return tree;
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean addApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
		return activity().addApps(t, id, uuid, appUuidIds);
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean removeApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
		return activity().removeApps(t, id, uuid, appUuidIds);
	}

}
