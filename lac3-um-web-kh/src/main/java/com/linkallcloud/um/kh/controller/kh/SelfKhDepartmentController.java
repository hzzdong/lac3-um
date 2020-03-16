package com.linkallcloud.um.kh.controller.kh;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.web.utils.Controllers;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;
import com.linkallcloud.core.www.ISessionUser;

@Controller
@RequestMapping(value = "/SelfKhDepartment", method = RequestMethod.POST)
@Module(name = "客户部门")
public class SelfKhDepartmentController {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhDepartmentManager khDepartmentManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhCompanyManager khCompanyManager;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "parentClass", required = false) String parentClass, Trace t, ModelMap modelMap) {
        ISessionUser su = Controllers.getSessionUser();
        modelMap.put("companyId", Long.valueOf(su.getCompanyId()));
        return edit(parentId, parentClass, null, null, t, modelMap);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "parentClass", required = false) String parentClass,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "uuid", required = false) String uuid, Trace t, ModelMap modelMap) {
        ISessionUser su = Controllers.getSessionUser();
        modelMap.put("companyId", Long.valueOf(su.getCompanyId()));
        modelMap.put("parentId", parentId);
        modelMap.put("parentClass", parentClass);
        modelMap.put("id", id);
        modelMap.put("uuid", uuid);
        return "page/khself/KhDepartment/edit";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody KhDepartment get(@RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "parentClass", required = false) String parentClass,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "uuid", required = false) String uuid, Trace t) {
        ISessionUser su = Controllers.getSessionUser();
        KhDepartment entity = null;
        if (id != null && id > 0 && uuid != null) {
            entity = khDepartmentManager.fetchByIdUuid(t, id, uuid);
        } else {
            entity = new KhDepartment();
            if (!Strings.isBlank(parentClass) && parentClass.equals(KhCompany.class.getSimpleName())) {// 登录用户所在公司下建部门
                entity.setParentId(null);
                entity.setParentClass(null);
            } else {
                entity.setParentId(parentId);
                entity.setParentClass(KhDepartment.class.getSimpleName());
                if (!entity.isTopParent()) {
                    KhDepartment parent = khDepartmentManager.fetchById(t, parentId);
                    if (parent != null) {
                        entity.setOrgName(parent.getName());
                    }
                }
            }
        }
        if (entity.isTopParent()) {
            KhCompany company = khCompanyManager.fetchById(t, Long.valueOf(su.getCompanyId()));
            if (company != null) {
                entity.setOrgName(company.getName());
            }
        }
        entity.setCompanyId(Long.valueOf(su.getCompanyId()));
        return entity;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody Tree save(@RequestBody @Valid KhDepartment entity, Trace t) {
        ISessionUser su = Controllers.getSessionUser();
        entity.setCompanyId(Long.valueOf(su.getCompanyId()));
        entity.setParentClass(entity.isTopParent() ? null : KhDepartment.class.getSimpleName());
        if (entity.getId() != null && entity.getId() > 0 && entity.getUuid() != null) {
            khDepartmentManager.update(t, entity);
        } else {
            Long id = khDepartmentManager.insert(t, entity);
            entity.setId(id);
        }
        return entity.toTreeNode();
    }

    @WebLog(db = true, desc = "用户([(${visitor.name})])删除了公司([(${id})]), TID:[(${tid})]")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody Boolean delete(@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid,
            Trace t) {
        return khDepartmentManager.delete(t, id, uuid);
    }

    // @WebLog(db = true, desc = "用户([(${visitor.name})])[#
    // th:if=\"${status==0}\"]启用了[/][# th:if=\"${status!=0}\"]停用了[/]菜单([(${id})]),
    // TID:[(${tid})]")
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public @ResponseBody Boolean changeStatus(@RequestParam(value = "status") int status,
            @RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t) {
        return khDepartmentManager.updateStatus(t, status, id, uuid);
    }

}
