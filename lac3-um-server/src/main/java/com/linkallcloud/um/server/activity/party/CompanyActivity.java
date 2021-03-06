package com.linkallcloud.um.server.activity.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.security.Securities;
import com.linkallcloud.core.util.Domains;
import com.linkallcloud.um.activity.party.ICompanyActivity;
import com.linkallcloud.um.constant.Consts;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.exception.AccountException;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.server.dao.party.ICompanyDao;
import com.linkallcloud.um.server.dao.party.IDepartmentDao;
import com.linkallcloud.um.server.dao.party.IUserDao;
import com.linkallcloud.um.server.dao.sys.IAccountDao;
import com.linkallcloud.um.server.dao.sys.IApplicationDao;
import com.linkallcloud.um.server.dao.sys.IAreaDao;

public abstract class CompanyActivity<T extends Company, CD extends ICompanyDao<T>, U extends User, UD extends IUserDao<U>, D extends Department, DD extends IDepartmentDao<D>, A extends Account, AD extends IAccountDao<A>>
        extends OrgActivity<T, CD, U, UD> implements ICompanyActivity<T> {

    @Autowired
    private IAreaDao areaDao;

    @Autowired
    protected IApplicationDao applicationDao;

    public CompanyActivity() {
        super();
    }

    protected abstract DD getDepartmentDao();

    protected abstract AD getAccountDao();

    @Transactional(readOnly = false)
    @Override
    public Boolean updateCompanyLogo(Trace t, Sid companyId, String logo) {
        int rows = dao().updateCompanyLogo(t, companyId.getId(), logo);
        return retBool(rows);
    }

    @Override
    public List<T> findSubCompanies(Trace t, Long companyId) {
        return dao().findByParent(t, companyId);
    }

    @Override
    public Long[] getCompanyAreaRootIds(Trace t, Long companyId) {
        Long[] roots = getConfigCompanyAreaRootIds(t, companyId);
        if (roots != null && roots.length > 0) {
            return roots;
        } else {
            T company = dao().fetchById(t, companyId);
            if (company == null) {
                throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "company参数错误");
            }
            if (company.isTopParent()) {
                return null;
            } else {
                return getCompanyAreaRootIds(t, company.getParentId());
            }
        }
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
    public Tree getTypedCompanyTree(Trace t, Long companyId, int[] orgTypes) {
        Tree ftree = getCompanyTree(t, Consts.ORG_TREE_TYPE_FULL, companyId);
        if (orgTypes != null && orgTypes.length > 0) {
            Tree typedTree = Trees.vroot(ftree.getName());
            for (int type : orgTypes) {
                Tree typeNode = Trees.orgTreeTypeFilter(ftree, type, true);
                if (typeNode != null) {
                    typedTree.addChild(typeNode);
                }
            }
            return typedTree;
        } else {
            return ftree;
        }
    }

    @Override
    public Tree loadCompanyOrgTree(Trace t, Long companyId) {
        return getCompanyTree(t, Consts.ORG_TREE_TYPE_COMPANY, companyId);
    }

    @Override
    public Tree getCompanyTree(Trace t, String treeType, Long comId) {
        T company = dao().fetchById(t, comId);
        if (company == null) {
            throw new ArgException("companyId参数错误");
        }

        Tree root = company.toTreeNode();
        root.setId("-" + company.getId());
        root.setpId(null);
        root.setOpen(true);

        List<Tree> children = null;
        if (Consts.ORG_TREE_TYPE_SELF.equals(treeType)) {
            children = getDepartmentChildren(t, company);
        } else if (Consts.ORG_TREE_TYPE_COMPANY.equals(treeType)) {
            children = getDirectSubCompanyAndDepartmentChildren(t, company);
        } else if (Consts.ORG_TREE_TYPE_FULL.equals(treeType)) {
            children = getSubCompanyAndDepartmentChildren(t, company);
        } else if (Consts.ORG_TREE_TYPE_FULL_COMPANY.equals(treeType)) {
            children = getSubCompanyChildren(t, company);
        }

        Trees.assembleTree(root, children);
        root.sort();
        return root;
    }

    @Override
    public List<Tree> getCompanyTreeList(Trace t, String treeType, Long comId) {
        T company = dao().fetchById(t, comId);
        if (company == null) {
            throw new ArgException("companyId参数错误");
        }

        Tree root = company.toTreeNode();
        root.setId("-" + company.getId());
        root.setpId(null);
        root.setOpen(true);

        List<Tree> children = null;
        if (Consts.ORG_TREE_TYPE_SELF.equals(treeType)) {
            children = getDepartmentChildren(t, company);
        } else if (Consts.ORG_TREE_TYPE_COMPANY.equals(treeType)) {
            children = getDirectSubCompanyAndDepartmentChildren(t, company);
        } else if (Consts.ORG_TREE_TYPE_FULL.equals(treeType)) {
            children = getSubCompanyAndDepartmentChildren(t, company);
        } else if (Consts.ORG_TREE_TYPE_FULL_COMPANY.equals(treeType)) {
            children = getSubCompanyChildren(t, company);
        }

        List<Tree> result = new ArrayList<Tree>();
        result.add(root);
        if (children != null && !children.isEmpty()) {
            result.addAll(children);
        }
        return result;
    }

    private List<Tree> getDepartmentChildren(Trace t, T company) {
        String rootId = "-" + company.getId();
        List<Tree> nodes = new ArrayList<Tree>();
        /* 部门树 */
        List<D> depts = getDepartmentDao().findCompanyDepartments(t, company.getId());
        if (depts != null && !depts.isEmpty()) {
            List<Tree> depTreeNodeList = Trees.assembleTreeList(rootId, depts);
            if (depTreeNodeList != null && !depTreeNodeList.isEmpty()) {
                nodes.addAll(depTreeNodeList);
            }
        }
        return nodes;
    }

    private List<Tree> getDirectSubCompanyChildren(Trace t, T company) {
        String rootId = "-" + company.getId();
        List<Tree> nodes = new ArrayList<Tree>();
        /* 直接子公司 */
        List<T> directCompanies = findDirectCompaniesByParentId(t, company.getId());
        if (directCompanies != null && !directCompanies.isEmpty()) {
            List<Tree> dcTreeNodeList = Trees.assembleDirectDomain(rootId, directCompanies, "-");
            if (dcTreeNodeList != null && !dcTreeNodeList.isEmpty()) {
                nodes.addAll(dcTreeNodeList);
            }
        }
        return nodes;
    }

    private List<Tree> getDirectSubCompanyAndDepartmentChildren(Trace t, T company) {
        List<Tree> result = new ArrayList<Tree>();
        List<Tree> cnodes = getDirectSubCompanyChildren(t, company);
        if (cnodes != null && !cnodes.isEmpty()) {
            result.addAll(cnodes);
        }

        List<Tree> dnodes = getDepartmentChildren(t, company);
        if (dnodes != null && !dnodes.isEmpty()) {
            result.addAll(dnodes);
        }
        return result;
    }

    private List<Tree> getSubCompanyAndDepartmentChildren(Trace t, T company) {
        String rootId = "-" + company.getId();
        List<Tree> nodes = new ArrayList<Tree>();

        Map<Long, T> companyIdsMap = new HashMap<>();
        companyIdsMap.put(company.getId(), company);

        /* 所有子公司 */
        List<T> allCompanies = findAllCompaniesByParentCode(t, company.getCode());
        if (allCompanies != null && !allCompanies.isEmpty()) {
            for (T node : allCompanies) {
                if (node.getStatus() != Domain.STATUS_DELETE) {
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

        /* 含子公司的所有部门树 */
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

    private List<Tree> getSubCompanyChildren(Trace t, T company) {
        String rootId = "-" + company.getId();
        List<Tree> nodes = new ArrayList<Tree>();

        /* 所有子公司 */
        List<T> allCompanies = findAllCompaniesByParentCode(t, company.getCode());
        if (allCompanies != null && !allCompanies.isEmpty()) {
            for (T node : allCompanies) {
                if (node.getStatus() != Domain.STATUS_DELETE) {
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
        return nodes;
    }

    @Override
    public Long[] findPermedCompanyAppMenus(Trace t, Long companyId, Long appId) {
        return dao().findPermedCompanyAppMenus(t, companyId, appId);
    }

    @Override
    public Boolean saveAppMenuPerm(Trace t, Sid com, Sid app, Map<String, Long> menuUuidIds) {
        T company = fetchByIdUuid(t, com.getId(), com.getUuid());
        Application application = applicationDao.fetchByIdUuid(t, app.getId(), app.getUuid());
        if (company != null && application != null) {
            dao().clearCompanyAppMenuPerms(t, com.getId(), app.getId());
            if (menuUuidIds != null && !menuUuidIds.isEmpty()) {
                List<Long> menuIds = Domains.parseIds(menuUuidIds);
                dao().saveCompanyAppMenuPerms(t, com.getId(), app.getId(), menuIds);
            }
            return true;
        }
        return false;
    }

    public boolean checkUserExist(Trace t, boolean isNew, T entity) {
        if (isNew) {
            if (!Strings.isBlank(entity.getJphone())) {
                U dbUser = getUserDao().fecthByAccount(t, entity.getJphone());
                if (dbUser != null) {
                    throw new AccountException(Exceptions.CODE_ERROR_PARAMETER, "账号已经存在，手机号码：" + entity.getJphone());
                }

                A account = getAccountDao().fecthByAccount(t, entity.getJphone());
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
                    entity.setHolidayId(parent.getHolidayId());
                }
            }
        } else {// 修改
            updateCodeIfModifiedParent(t, entity);
            // 若area更改了，则更新AreaFields
            updateAreaFieldsIfModified(t, entity);
        }
        dealFullName(t, isNew, entity);
    }

    protected void dealFullName(Trace t, boolean isNew, T entity) {
        if (Strings.isBlank(entity.getFullName())) {
            // if (entity.getAreaId() != null) {
            // Area area = areaDao.fetchById(t, entity.getAreaId());
            // if (area != null) {
            // entity.setFullName(area.getName() + entity.getName());
            // }
            // } else {
            // entity.setFullName(entity.getName());
            // }
            if (entity.getParentId() != null) {
                T parent = dao().fetchById(t, entity.getParentId());
                if (parent != null) {
                    entity.setFullName(parent.getName() + entity.getName());
                }
            } else {
                entity.setFullName(entity.getName());
            }
        }
    }

    /**
     * 若area更改了，则更新AreaFields
     *
     * @param t
     * @param entity
     */
    protected void updateAreaFieldsIfModified(Trace t, T entity) {
        if (entity == null) {
            return;
        }
        T dbEntity = dao().fetchById(t, entity.getId());
        if (dbEntity == null) {
            return;
        }

        if (!dbEntity.getAreaId().equals(entity.getAreaId())) {
            updateAreaFields(t, entity);
        }
    }

    /**
     * 处理area冗余字段
     *
     * @param t
     * @param entity
     */
    protected void updateAreaFields(Trace t, T entity) {
        if (entity == null || entity.getAreaId() == null || entity.getAreaId().longValue() <= 0) {
            return;
        }
        Area area = areaDao.fetchById(t, entity.getAreaId());
        if (area != null) {
            entity.setLevel(area.getLevel());
            String[] aids = area.getCode().split(area.codeTag());
            if (aids != null && aids.length > 0) {
                for (int i = 0; i < aids.length; i++) {
                    String aid = aids[i];
                    if (!Strings.isBlank(aid)) {
                        entity.setAreaLevelId(i + 1, Long.parseLong(aid));
                    }
                }
            }
            dao().updateAreaFields(t, entity);
        }
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

    protected abstract void autoCreateAccount(Trace t, U user);

    // protected void autoCreateAccount(Trace t, User user) {
    // Account account = new Account(user.getName(), user.getMobile(), user.getAccount(), user.getPassword(),
    // user.getSalt());
    // accountDao.insert(t, account);
    // }

    private List<Application> findAppsByUuidIds(Trace t, Map<String, Long> appUuidIds) {
        List<Long> ids = Domains.parseIds(appUuidIds);
        if (ids != null && ids.size() > 0) {
            List<Application> entities = applicationDao.findByIds(t, ids);
            if (entities != null && !entities.isEmpty()) {
                List<Application> results = new ArrayList<Application>();
                for (Application entity : entities) {
                    if (entity.getUuid() != null && entity.getId().equals(appUuidIds.get(entity.getUuid()))) {
                        results.add(entity);
                    }
                }
                return results;
            }
        }
        return null;
    }

    @Transactional(readOnly = false)
    @Override
    public Boolean addApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
        T company = fetchByIdUuid(t, id, uuid);
        if (company != null) {
            List<Application> checkedEntities = findAppsByUuidIds(t, appUuidIds);
            if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == appUuidIds.size()) {
                List<Long> appIds = Domains.parseIds(appUuidIds);
                int rows = dao().addApps(t, id, appIds);
                return retBool(rows);
            }
        }
        return false;
    }

    @Transactional(readOnly = false)
    @Override
    public Boolean removeApps(Trace t, Long id, String uuid, Map<String, Long> appUuidIds) {
        T company = fetchByIdUuid(t, id, uuid);
        if (company != null) {
            List<Application> checkedEntities = findAppsByUuidIds(t, appUuidIds);
            if (checkedEntities != null && !checkedEntities.isEmpty() && checkedEntities.size() == appUuidIds.size()) {
                List<Long> appIds = Domains.parseIds(appUuidIds);
                int rows = dao().removeApps(t, id, appIds);
                return retBool(rows);
            }
        }
        return false;
    }

    @Transactional(readOnly = false)
    @Override
    public void updateChildrenCompaniesHolidaySetup(Trace t, Long companyId) {
        T company = fetchById(t, companyId);
        if (company != null) {
            List<Long> childrenCompanyIds = new ArrayList<Long>();
            childrenCompanyIds.add(companyId);
            findChildrenCompanyIds(t, company, childrenCompanyIds);

            updateHolidays(t, companyId, childrenCompanyIds);
        }
    }

    private void findChildrenCompanyIds(Trace t, T parentCompany, List<Long> childrenCompanyIds) {
        List<T> children = this.findDirectCompaniesByParentId(t, parentCompany.getId());
        if (children != null && !children.isEmpty()) {
            for (T child : children) {
                if (child.getHolidayId() == null) {// child没设置过
                    childrenCompanyIds.add(child.getId());
                    findChildrenCompanyIds(t, child, childrenCompanyIds);
                } else {
                    if (child.getHolidayId().equals(child.getId())) {// child自己设置了自己的
                        // child不变
                    } else {
                        if (parentCompany.getHolidayId() == null) {// parent没设置过
                            // child不变
                        } else {
                            if (child.getHolidayId().equals(parentCompany.getHolidayId())) {// child和parent之前设置的相同
                                childrenCompanyIds.add(child.getId());
                                findChildrenCompanyIds(t, child, childrenCompanyIds);
                            } else {
                                // child不变
                            }
                        }
                    }
                }
            }
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void updateHolidays(Trace t, Long holidayId, List<Long> companyIds) {
        dao().updateHolidays(t, holidayId, companyIds);
    }

}
