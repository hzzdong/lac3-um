package com.linkallcloud.um.server.service.party;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.um.activity.party.ICompanyActivity;
import com.linkallcloud.um.activity.party.IDepartmentActivity;
import com.linkallcloud.um.activity.party.IUserActivity;
import com.linkallcloud.um.activity.sys.IAccountActivity;
import com.linkallcloud.um.activity.sys.IApplicationActivity;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.Department;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.service.party.ICompanyService;

@Transactional(readOnly = true)
public abstract class CompanyService<C extends Company, CA extends ICompanyActivity<C>, U extends User, UA extends IUserActivity<U>, D extends Department, DA extends IDepartmentActivity<D>>
        extends OrgService<C, CA, U, UA> implements ICompanyService<C> {

    @Autowired
    protected IAccountActivity accountActivity;

//    @Autowired
//    private IAreaActivity areaActivity;

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
    public Long[] getCompanyAreaRootIds(Trace t, Long companyId, Long appId) {
        return activity().getCompanyAreaRootIds(t, companyId, appId);
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
    public List<Tree> getCompanyFullOrgTreeList(Trace t, Long companyId) {
        return activity().getCompanyFullOrgTreeList(t, companyId);
    }

    @Override
    public List<Tree> getCompanyOrgTreeList(Trace t, Long companyId) {
        return activity().getCompanyOrgTreeList(t, companyId);
    }

    @Override
    public Long[] findPermedCompanyAppMenus(Trace t, Long companyId, Long appId) {
        return activity().findPermedCompanyAppMenus(t, companyId, appId);
    }

    @Transactional(readOnly = false)
    @Override
    public Boolean saveCompanyAppMenuPerm(Trace t, Long id, String uuid, Long appId, String appUuid,
                                          Map<String, Long> menuUuidIds) {
        return activity().saveCompanyAppMenuPerm(t, id, uuid, appId, appUuid, menuUuidIds);
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
    // List<C> directCompanies = findDirectCompaniesByParentId(t, companyId);
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

}
