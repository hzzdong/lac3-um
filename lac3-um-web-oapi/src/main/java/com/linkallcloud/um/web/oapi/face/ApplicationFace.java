package com.linkallcloud.um.web.oapi.face;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Menu;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IMenuManager;
import com.linkallcloud.um.iapi.sys.IOperationManager;
import com.linkallcloud.web.face.annotation.Face;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/face/Application", method = RequestMethod.POST)
@Module(name = "应用")
public class ApplicationFace {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IApplicationManager applicationManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IOperationManager operationManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IMenuManager menuManager;

    @Face(login = false)
    @RequestMapping(value = "/fetchById", method = RequestMethod.POST)
    public @ResponseBody
    Object fetchById(IdFaceRequest faceReq, Trace t) throws Exception {
        if (Strings.isBlank(faceReq.getId())) {
            return null;
        }
        Application app = applicationManager.fetchById(t, Long.parseLong(faceReq.getId()));
        return desensitization(t, faceReq, app);
    }

    @Face(login = false)
    @RequestMapping(value = "/fetch", method = RequestMethod.POST)
    public @ResponseBody
    Object fetch(ObjectFaceRequest<String> faceReq, Trace t) throws Exception {
        String appCode = faceReq.getData();
        Application app = applicationManager.fetchByCode(t, appCode);
        return desensitization(t, faceReq, app);
    }

    @Face(login = false)
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public @ResponseBody
    Object findAll(ListFaceRequest faceReq, Trace t) throws Exception {
        if (isInnerVisitor(t, faceReq)) {
            Query q = new Query();
            q.addRule(new Equal("status", 0));
            List<Application> apps = applicationManager.find(t, q);
            return desensitizations(t, faceReq, apps);
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public @ResponseBody
    Object find(ListFaceRequest faceReq, Trace t) throws Exception {
        if (isInnerVisitor(t, faceReq)) {
            Query q = new Query();
            WebQuery wq = faceReq.getQuery();
            if (wq != null) {
                q = wq.toQuery();
            }
            List<Application> apps = applicationManager.find(t, q);
            return desensitizations(t, faceReq, apps);
        }
        return null;
    }

    /**
     * 判断是否内部应用访问
     *
     * @param t
     * @param faceReq
     * @return
     */
    private boolean isInnerVisitor(Trace t, FaceRequest faceReq) {
        String visitorAppCode = faceReq.getAppCode();
        Application visitorApp = applicationManager.fetchByCode(t, visitorAppCode);
        if (visitorApp != null && visitorApp.getType() == 0) {// 内部应用
            return true;
        }
        return false;
    }

    @Face(login = false)
    @RequestMapping(value = "/loadAppUriRescodeMap", method = RequestMethod.POST)
    public @ResponseBody
    Object loadAppUriRescodeMap(IdFaceRequest faceReq, Trace t) throws Exception {
        if (Strings.isBlank(faceReq.getId())) {
            return null;
        }
        Map<String, String[]> result = operationManager.loadAppUriRescodeMap(t, Long.parseLong(faceReq.getId()));
        return result;
    }

    @Face(login = false)
    @RequestMapping(value = "/getAppMenuTree", method = RequestMethod.POST)
    public @ResponseBody
    Object getAppMenuTree(ObjectFaceRequest<String> faceReq, Trace t) throws Exception {
        String appCode = faceReq.getData();
        return menuManager.getMenuTree(t, appCode);
    }

    @Face(login = false)
    @RequestMapping(value = "/getAppMenus", method = RequestMethod.POST)
    public @ResponseBody
    Object getAppMenus(ObjectFaceRequest<String> faceReq, Trace t) throws Exception {
        String appCode = faceReq.getData();
        List<Menu> menus = menuManager.getValidMenuList(t, appCode);
        return menus;
    }

    @Face(login = false)
    @RequestMapping(value = "/findByYwUserId", method = RequestMethod.POST)
    public @ResponseBody
    Object findByYwUserId(IdFaceRequest faceReq, Trace t) throws Exception {
        if (Strings.isBlank(faceReq.getId())) {
            return null;
        }
        if (isInnerVisitor(t, faceReq)) {
            List<Application> apps = applicationManager.find4YwUser(t, Long.parseLong(faceReq.getId()));
            return desensitizations(t, faceReq, apps);
        }
        return null;
    }

    /**
     * 访问者appcode匹配，并脱敏
     *
     * @param faceReq
     * @param app
     * @return
     */
    private Application desensitization(Trace t, FaceRequest faceReq, Application app) {
        if (isInnerVisitor(t, faceReq)) {
            return app;
        } else {
            if (app != null && app.getCode().equals(faceReq.getAppCode())) {
                app.desensitization();
                return app;
            }
            return null;
        }
    }

    /**
     * 访问者appcode匹配，并脱敏
     *
     * @param t
     * @param faceReq
     * @param apps
     * @return
     */
    private List<Application> desensitizations(Trace t, FaceRequest faceReq, List<Application> apps) {
        if (isInnerVisitor(t, faceReq)) {
            return apps;
        } else {
            List<Application> result = new ArrayList<>();
            if (apps != null && apps.size() > 0) {
                for (Application app : apps) {
                    if (app.getCode().equals(faceReq.getAppCode())) {
                        app.desensitization();
                        result.add(app);
                    }
                }
            }
            return result;
        }
    }

}
