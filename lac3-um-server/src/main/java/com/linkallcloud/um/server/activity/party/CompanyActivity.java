package com.linkallcloud.um.server.activity.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.security.Securities;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.activity.party.ICompanyActivity;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.dto.base.PermedAreaVo;
import com.linkallcloud.um.exception.AccountException;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.exception.AuthException;
import com.linkallcloud.um.server.dao.party.ICompanyDao;
import com.linkallcloud.um.server.dao.party.IDepartmentDao;
import com.linkallcloud.um.server.dao.party.IUserDao;
import com.linkallcloud.um.server.dao.sys.IAccountDao;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import com.linkallcloud.um.server.dao.sys.IAreaDao;

public abstract class CompanyActivity<T extends Company, CD extends ICompanyDao<T>, U extends User, UD extends IUserDao<U>, D extends Department, DD extends IDepartmentDao<D>>
		extends OrgActivity<T, CD, U, UD> implements ICompanyActivity<T> {

	@Autowired
	protected IAccountDao accountDao;

	@Autowired
	private IAreaDao areaDao;

	@Autowired
	protected IApplicationDao applicationDao;

	public CompanyActivity() {
		super();
	}

	protected abstract DD getDepartmentDao();

	@Override
	public List<T> findSubCompanies(Trace t, Long companyId) {
		return dao().findByParent(t, companyId);
	}

	@Override
	public Long getCompanyAreaRootId(Trace t, Long companyId, Long appId) {
		T company = dao().fetchById(t, companyId);
		if (company.isTopParent()) {
			return getCompanyAreaRootIdBySystemConfig(t, companyId);
		} else {
			Long[] permedItemIds = findPermedCompanyAppAreas(t, companyId, appId);
			if (permedItemIds != null && permedItemIds.length > 0) {
				Area area = areaDao.fetchById(t, permedItemIds[0]);
				return area.getParentId();
			} else {
				throw new AuthException("100001", "请先给单位区域授权后再进行此操作");
			}
		}
	}

	@Override
	public Long[] getCompanyAreaRootIds(Trace t, Long companyId, Long appId) {
		T company = dao().fetchById(t, companyId);
		if (company.isTopParent()) {
			Long[] companyAreaRootIds = new Long[0];
			Long areaId = getCompanyAreaRootIdBySystemConfig(t, companyId);
			if (areaId != null) {
				companyAreaRootIds = new Long[1];
				companyAreaRootIds[0] = areaId;
			}
			return companyAreaRootIds;
		} else {
			Long[] permedItemIds = findPermedCompanyAppAreas(t, companyId, appId);
			if (permedItemIds != null && permedItemIds.length > 0) {
				return permedItemIds;
			} else {
				throw new AuthException("100001", "请先给单位区域授权后再进行此操作");
			}
		}
	}

	@Override
	public Long[] findPermedCompanyAppAreas(Trace t, Long companyId, Long appId) {
		return dao().findPermedCompanyAppAreas(t, companyId, appId);
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean saveCompanyAppAreaPerm(Trace t, Long companyId, String companyUuid, Long appId, String appUuid,
			Map<String, Long> areaUuidIds) {
		T company = fetchByIdUuid(t, companyId, companyUuid);
		Application app = applicationDao.fetchByIdUuid(t, appId, appUuid);
		if (company != null && app != null) {
			dao().clearCompanyAppAreaPerms(t, companyId, appId);
			if (areaUuidIds != null && !areaUuidIds.isEmpty()) {
				List<Long> areaIds = Domains.parseIds(areaUuidIds);
				dao().saveCompanyAppAreaPerms(t, companyId, appId, areaIds);
			}
			return true;
		}
		return false;
	}

	protected PermedAreaVo assemblePermedAreaVo(Trace t, Long parentAreaId, List<Area> areas) {
		PermedAreaVo result = new PermedAreaVo();
		result.setParentAreaId(parentAreaId);
		if (0L != result.getParentAreaId()) {
			Area parent = areaDao.fetchById(t, result.getParentAreaId());
			if (parent != null) {
				result.setParentAreaName(parent.getName());
			}
		} else {
			result.setParentAreaName("中华人民共和国");
		}

		if (areas != null && !areas.isEmpty()) {
			List<Tree> anodes = new ArrayList<>();
			for (int i = 0; i < areas.size(); i++) {
				Tree tn = areas.get(i).toTreeNode();
				tn.setpId(null);
				anodes.add(tn);
			}
			result.setAreaNodes(Trees.filterTreeNode(anodes));
		}
		return result;
	}

	// @Override
	// public PermedAreaVo findValidAreaResourceByParent(Trace t, Long parentAreaId)
	// {
	// List<Area> areas = areaDao.findByParent(t, parentAreaId);
	// return assemblePermedAreaVo(t, parentAreaId, areas);
	// }

	@Override
	public List<Tree> findCompanyValidOrgResource(Trace t, Long companyId) {
		return getCompanyOrgTreeList(t, companyId);
	}

	@Override
	public List<T> findDirectCompaniesByParentId(Trace t, Long parentId) {
		return dao().findByParent(t, parentId);
	}

	@Override
	public List<T> findAllCompaniesByParentCode(Trace t, String govCode) {
		return dao().findAllCompaniesByParentCode(t, govCode, govCode.length());
	}

	@Override
	public Tree getCompanyFullOrgTree(Trace t, Long companyId) {
		T company = dao().fetchById(t, companyId);
		if (company == null) {
			throw new ArgException("companyId参数错误");
		}

		Tree root = company.toTreeNode();
		root.setId("-" + company.getId());
		root.setpId(null);
		root.setOpen(true);

		List<Tree> children = getCompanyChildrenFullTreeNodes(t, company);
		Trees.assembleTree(root, children);
		root.sort();

		return root;
	}

	@Override
	public List<Tree> getCompanyFullOrgTreeList(Trace t, Long companyId) {
		T company = dao().fetchById(t, companyId);
		if (company == null) {
			throw new ArgException("companyId参数错误");
		}

		Tree root = company.toTreeNode();
		root.setId("-" + company.getId());
		root.setpId(null);
		root.setOpen(true);

		List<Tree> items = getCompanyChildrenFullTreeNodes(t, company);
		items.add(root);

		items = Trees.filterTreeNode(items);
		return items;
	}

	private Tree getCompanyTreeNode(Trace t, Long companyId) {
		T company = dao().fetchById(t, companyId);
		if (company == null) {
			throw new ArgException("companyId参数错误");
		}

		Tree root = company.toTreeNode();
		root.setId("-" + company.getId());
		root.setpId(null);
		root.setOpen(true);
		return root;
	}

	private List<Tree> getCompanyChildrenTreeNodes(Trace t, Long companyId) {
		String rootId = "-" + companyId;
		List<Tree> nodes = new ArrayList<Tree>();

		/* 直接子公司 */
		List<T> directCompanies = findDirectCompaniesByParentId(t, companyId);
		if (directCompanies != null && !directCompanies.isEmpty()) {
			List<Tree> dcTreeNodeList = Trees.assembleDirectTreeNodeList(rootId, directCompanies, "-");
			if (dcTreeNodeList != null && !dcTreeNodeList.isEmpty()) {
				nodes.addAll(dcTreeNodeList);
			}
		}

		/* 部门树 */
		List<D> depts = getDepartmentDao().findCompanyDepartments(t, companyId);
		if (depts != null && !depts.isEmpty()) {
			List<Tree> depTreeNodeList = Trees.assembleTreeList(rootId, depts);
			if (depTreeNodeList != null && !depTreeNodeList.isEmpty()) {
				nodes.addAll(depTreeNodeList);
			}
		}

		return nodes;
	}

	private List<Tree> getCompanyChildrenFullTreeNodes(Trace t, T company) {
		String rootId = "-" + company.getId();
		List<Tree> nodes = new ArrayList<Tree>();

		Map<Long, T> companyIdsMap = new HashMap<>();
		companyIdsMap.put(company.getId(), company);

		/* 所有子公司 */
		List<T> allCompanies = findAllCompaniesByParentCode(t, company.getCode());
		if (allCompanies != null && !allCompanies.isEmpty()) {
			for (T node : allCompanies) {
				if (node.getStatus() == 0) {
					companyIdsMap.put(node.getId(), node);
					if (node.isTopParent()) {
						Tree item = node.toTreeNode();
						item.setId("-" + item.getId());
						item.setpId(rootId);
						nodes.add(item);
					} else {
						Tree item = node.toTreeNode();
						item.setId("-" + item.getId());
						item.setpId("-" + item.getpId());
						nodes.add(item);
					}
				}
			}
		}

		/* 部门树 */
		List<D> depts = getDepartmentDao().findAllDepartments(t, new ArrayList<>(companyIdsMap.keySet()));
		if (depts != null && !depts.isEmpty()) {
			for (D enode : depts) {
				Tree tnode = enode.toTreeNode();
				T myCompany = companyIdsMap.get(enode.getCompanyId());
				if (myCompany != null) {
					tnode.addAttribute("areaId", myCompany.getAreaId());

					if (enode.getParentId() == null || enode.getParentId().equals(0L)
							|| Strings.isBlank(enode.getParentClass())) {
						tnode.setpId("-" + enode.getCompanyId());
					}
					nodes.add(tnode);
				}
			}
		}

		return nodes;
	}

	@Override
	public Tree getCompanyOrgTrees(Trace t, Long companyId) {
		Tree root = getCompanyTreeNode(t, companyId);
		List<Tree> children = getCompanyChildrenTreeNodes(t, companyId);
		Trees.assembleTree(root, children);
		root.sort();
		return root;
	}

	@Override
	public List<Tree> getCompanyOrgTreeList(Trace t, Long companyId) {
		Tree root = getCompanyTreeNode(t, companyId);
		List<Tree> children = getCompanyChildrenTreeNodes(t, companyId);
		return new ArrayList<Tree>() {
			private static final long serialVersionUID = 6141538954506052171L;
			{
				add(root);
				addAll(children);
			}
		};
	}

	@Override
	public Long[] findPermedCompanyAppMenus(Trace t, Long companyId, Long appId) {
		return dao().findPermedCompanyAppMenus(t, companyId, appId);
	}

	@Transactional(readOnly = false)
	@Override
	public Boolean saveCompanyAppMenuPerm(Trace t, Long id, String uuid, Long appId, String appUuid,
			Map<String, Long> menuUuidIds) {
		T company = fetchByIdUuid(t, id, uuid);
		Application app = applicationDao.fetchByIdUuid(t, appId, appUuid);
		if (company != null && app != null) {
			dao().clearCompanyAppMenuPerms(t, id, appId);
			if (menuUuidIds != null && !menuUuidIds.isEmpty()) {
				List<Long> menuIds = Domains.parseIds(menuUuidIds);
				dao().saveCompanyAppMenuPerms(t, id, appId, menuIds);
			}
			return true;
		}
		return false;
	}

	// @Override
	// public Tree getCompanyOrgTree(Trace t, Long companyId) {
	// T company = dao().fetchById(t, companyId);
	// if (company == null) {
	// return null;
	// }
	//
	// Tree root = new Tree("COM-" + company.getId(), company.getUuid(), null,
	// company.getName(), company.getGovCode(),
	// String.valueOf(Domains.ORG_COMPANY), company.getStatus());
	//
	// /* 直接子公司 */
	// List<T> directCompanies = findDirectCompaniesByParentId(t, companyId);
	// if (directCompanies != null && !directCompanies.isEmpty()) {
	// Trees.assembleDirectTreeNode(root, directCompanies, "COM-");
	// }
	//
	// /* 部门树 */
	// List<D> depts = getDepartmentDao().findCompanyDepartments(t, companyId);
	// if (depts != null && !depts.isEmpty()) {
	// CopyOnWriteArrayList<D> departments = new CopyOnWriteArrayList<D>(depts);
	// depts.clear();
	// // assembleDepartmentTree(root, departments);
	// Trees.assembleTree(root, departments);
	// }
	//
	// return root;
	// }

	// private void assembleDepartmentTree(Tree parent, CopyOnWriteArrayList<D>
	// departments) {
	// if (parent != null && departments != null) {
	// for (D dep : departments) {
	// if (parent.getType().equals(String.valueOf(Domains.ORG_COMPANY))) {
	// if (dep.isTopParent()) {
	// Tree child = new Tree(dep.getId().toString(), parent.getId(), dep.getName(),
	// String.valueOf(Domains.ORG_DEPARTMENT));
	// parent.addChild(child);
	// departments.remove(dep);
	// }
	// } else {
	// if (dep.getParentId() != null &&
	// dep.getParentId().toString().equals(parent.getId())) {
	// Tree child = new Tree(dep.getId().toString(), parent.getId(), dep.getName(),
	// String.valueOf(Domains.ORG_DEPARTMENT));
	// parent.addChild(child);
	// departments.remove(dep);
	// }
	// }
	// }
	//
	// if (parent.getChildren() != null && parent.getChildren().size() > 0 &&
	// departments.size() > 0) {
	// for (Tree item : parent.getChildren()) {
	// assembleDepartmentTree(item, departments);
	// }
	// }
	// }
	// }

	public boolean checkUserExist(Trace t, boolean isNew, T entity) {
		if (isNew) {
			if (!Strings.isBlank(entity.getJphone())) {
				U dbUser = getUserDao().fecthByAccount(t, entity.getJphone());
				if (dbUser != null) {
					throw new AccountException(Exceptions.CODE_ERROR_PARAMETER, "账号已经存在，手机号码：" + entity.getJphone());
				}

				Account account = accountDao.fecthByAccount(t, entity.getJphone());
				if (account != null) {
					throw new AccountException(Exceptions.CODE_ERROR_PARAMETER, "账号已经存在，手机号码：" + entity.getJphone());
				}
			}
		}
		return false;
	}

	@Override
	protected void treeBefore(Trace t, boolean isNew, T entity) throws BaseRuntimeException {
		checkUserExist(t, isNew, entity);
		if (isNew) {
			if (!Strings.isBlank(entity.getGovCode())) {
				T dbEntity = dao().fetchByGovCode(t, entity.getGovCode());
				if (dbEntity != null) {
					throw new AccountException("10000001", "已经存在相同govCode的节点：" + entity.getGovCode());
				}
			}
			if (!Strings.isBlank(entity.getJphone())) {
				U dbUser = getUserDao().fecthByAccount(t, entity.getJphone());
				if (dbUser != null) {
					throw new AccountException(Exceptions.CODE_ERROR_PARAMETER, "账号已经存在，手机号码：" + entity.getJphone());
				}

			}

			if (entity.isTopParent()) {
				entity.setLevel(1);
			} else {
				T parent = dao().fetchById(t, entity.getParentId());
				if (parent != null) {
					entity.setLevel(parent.getLevel() + 1);
				}
			}
		} else {// 修改
			updateCodeIfModifiedParent(t, entity);
			// 若area更改了，则更新AreaFields
			updateAreaFieldsIfModified(t, entity);
		}
	}

	/**
	 * 若area更改了，则更新AreaFields
	 *
	 * @param t
	 * @param entity
	 */
	protected void updateAreaFieldsIfModified(Trace t, T entity) {
	}

	/**
	 * 处理area冗余字段
	 *
	 * @param t
	 * @param entity
	 */
	protected void updateAreaFields(Trace t, T entity) {
	}

	@Override
	protected void updateCodeIfModifiedParent(Trace t, T entity) {
		boolean parentChanged = false;
		T dbEntity = dao().fetchById(t, entity.getId());
		if (dbEntity.isTopParent()) {
			if (!entity.isTopParent()) {
				parentChanged = true;
			}
		} else {
			if (!dbEntity.getParentId().equals(entity.getParentId())) {
				parentChanged = true;
			}
		}

		if (parentChanged) {
			if (entity.isTopParent()) {
				entity.setCode(Domains.genDomainCode(null, entity));
			} else {
				T parent = dao().fetchById(t, entity.getParentId());
				if (parent != null) {
					entity.setCode(Domains.genDomainCode(parent, entity));
				} else {
					throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "parentId参数错误。");
				}
			}
			updateCode(t, entity.getId(), entity.getCode());
		}
	}

	@Override
	protected void treeAfter(Trace t, boolean isNew, T entity) throws BaseRuntimeException {
		// super.treeAfter(t, isNew, entity);
		if (isNew) {// 新增
			if (entity.isTopParent()) {
				entity.setCode(Domains.genDomainCode(null, entity));
			} else {
				T parent = dao().fetchById(t, entity.getParentId());
				if (parent != null) {
					entity.setCode(Domains.genDomainCode(parent, entity));
				} else {
					throw new ArgException(Exceptions.CODE_ERROR_PARAMETER, "parentId参数错误。");
				}
			}
			updateCode(t, entity.getId(), entity.getCode());

			// 处理area冗余字段
			updateAreaFields(t, entity);

			// 创建管理员账号
			autoCreateAdmin(t, entity);
		}

		// 自动创建公司相关其它账户。若修改的时候发现没有创建，也会进行创建。
		autoCreateOtherAccount4Village(t, entity);
	}

	protected void autoCreateOtherAccount4Village(Trace t, T entity) {
	}

	/**
	 * 自动创建管理员账号
	 *
	 * @param t
	 * @param entity
	 */
	protected void autoCreateAdmin(Trace t, T entity) {
		U user = userMirror.born(entity.getName() + "_管理员", entity.getJphone(), entity.getJphone(), entity.getJphone());
		user.setSalt(user.generateUuid());
		user.setPassword(Securities.password4Src(user.getPassword(), user.getSalt()));
		user.setCompanyId(entity.getId());
		user.setParentId(entity.getId());
		user.setParentClass(entity.getClass().getSimpleName());
		user.setType(Domains.USER_ADMIN);
		getUserDao().insert(t, user);

		user.setCode(Domains.genDomainCode(entity, user));
		getUserDao().updateCode(t, user.getId(), user.getCode());

		autoCreateAccount(t, user);
		autoAddSysAdminRole(t, user);
	}

	/**
	 * 自动分配系统管理员角色
	 *
	 * @param t
	 * @param user
	 */
	protected abstract void autoAddSysAdminRole(Trace t, U user);

	protected void autoCreateAccount(Trace t, User user) {
		Account account = new Account(user.getName(), user.getMobile(), user.getAccount(), user.getPassword(),
				user.getSalt());
		accountDao.insert(t, account);
	}

}
