package com.linkallcloud.um.pc.controller.sys;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.web.controller.BaseLController;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.domain.sys.Operation;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IMenuManager;
import com.linkallcloud.um.iapi.sys.IOperationManager;

@Controller
@RequestMapping(value = "/Operation", method = RequestMethod.POST)
@Module(name = "操作")
public class OperationController extends BaseLController<Operation, IOperationManager> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IOperationManager operationManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IMenuManager menuManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IApplicationManager appliactionManager;

    @Override
    protected IOperationManager manager() {
        return operationManager;
    }

    @Override
    protected String doMain(boolean prepare, Long parentId, String parentClass, Trace t, ModelMap modelMap,
            AppVisitor av) {
        if (parentId != null) {
            Menu menu = menuManager.fetchById(t, parentId);
            modelMap.put("menuId", menu.getId());
            modelMap.put("menuUuid", menu.getUuid());
            modelMap.put("menuName", menu.getName());
            if (menu != null && menu.getAppId() != null) {
                Application app = appliactionManager.fetchById(t, menu.getAppId());
                modelMap.put("appId", app.getId());
                modelMap.put("appUuid", app.getUuid());
                // try {
                // List<Tree> items = menuManager.getValidMenus(t, app.getId(), app.getUuid());
                // } catch (IllegalParameterException e) {
                // }
            }
        }

        return super.doMain(prepare, parentId, parentClass, t, modelMap, av);
    }

    @Override
    protected Operation doGet(Long parentId, String parentClass, Long id, String uuid, Trace t, AppVisitor av) {
        Operation entity = super.doGet(parentId, parentClass, id, uuid, t, av);
        if (entity.getMenuId() == null) {
            Menu menu = menuManager.fetchById(t, parentId);
            if (menu != null) {
                entity.setMenuId(parentId);
                entity.setMenuName(menu.getName());
            }
        }
        return entity;
    }

    @Override
    protected Operation doSave(Operation entity, Trace t, AppVisitor av) {
        if (entity.getMenuId() != null) {
            Menu menu = menuManager.fetchById(t, entity.getMenuId());
            if (menu != null) {
                entity.setAppId(menu.getAppId());
                entity.setMenuGovCode(menu.getGovCode());
                entity.setMenuName(menu.getName());
            }
        }
        return super.doSave(entity, t, av);
    }

}
