package com.linkallcloud.um.web.oapi.face.party;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.um.domain.party.YwRole;
import com.linkallcloud.um.iapi.party.IYwRoleManager;
import com.linkallcloud.web.face.annotation.Face;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/face/YwRole", method = RequestMethod.POST)
@Module(name = "运维角色")
public class YwRoleFace {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IYwRoleManager ywRoleManager;

    @Face(login = false)
    @RequestMapping(value = "/fetchById", method = RequestMethod.POST)
    public @ResponseBody
    Object fetchById(IdFaceRequest faceReq, Trace t) throws Exception {
        if (faceReq.getId()!=null) {
            return null;
        }
        return ywRoleManager.fetchById(t, faceReq.getId());
    }

    @Face(login = false)
    @RequestMapping(value = "/fetchByGovCode", method = RequestMethod.POST)
    public @ResponseBody
    Object fetchByGovCode(ObjectFaceRequest<String> faceReq, Trace t) throws Exception {
        String govCode = faceReq.getData();
        if (!Strings.isBlank(govCode)) {
            return ywRoleManager.fetchByGovCode(t, govCode);
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public @ResponseBody
    Object find(ListFaceRequest faceReq, Trace t) throws Exception {
        WebQuery wq = faceReq.getQuery();
        if (wq != null) {
            return ywRoleManager.find(t, wq.toQuery());
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public @ResponseBody
    Object findPage(PageFaceRequest faceReq, Trace t) throws Exception {
        Page<YwRole> page = new Page<>(faceReq);
        return ywRoleManager.findPage(t, page);
    }

    @Face(login = false)
    @RequestMapping(value = "/findPage4Select", method = RequestMethod.POST)
    public @ResponseBody
    Object findPage4Select(PageFaceRequest faceReq, Trace t) throws Exception {
        Page<YwRole> page = new Page<>(faceReq);
        return ywRoleManager.findPage4Select(t, page);
    }

    @Face(login = false)
    @RequestMapping(value = "/find4User", method = RequestMethod.POST)
    public @ResponseBody
    Object find4User(IdFaceRequest faceReq, Trace t) throws Exception {
        if (faceReq.getId()!=null) {
            return null;
        }
        return ywRoleManager.find4User(t, faceReq.getId());
    }

}
