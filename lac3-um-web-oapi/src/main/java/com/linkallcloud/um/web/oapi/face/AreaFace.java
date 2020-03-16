package com.linkallcloud.um.web.oapi.face;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.um.face.area.ParentCodeAreaRequest;
import com.linkallcloud.um.face.area.ParentCodeLevelAreaRequest;
import com.linkallcloud.um.face.area.ParentIdAreaRequest;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.web.face.annotation.Face;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/face/Area", method = RequestMethod.POST)
@Module(name = "区域")
public class AreaFace {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IAreaManager areaManager;

    @Face(login = false)
    @RequestMapping(value = "/getTreeNodes", method = RequestMethod.POST)
    public @ResponseBody
    Object getTreeNodes(ObjectFaceRequest<Boolean> faceReq, Trace t) {
        return areaManager.getTreeNodes(t, faceReq.getData());
    }

    @Face(login = false)
    @RequestMapping(value = "/fetchById", method = RequestMethod.POST)
    public @ResponseBody
    Object fetchById(IdFaceRequest faceReq, Trace t) {
        if (Strings.isBlank(faceReq.getId())) {
            return null;
        }
        return areaManager.fetchById(t, Long.parseLong(faceReq.getId()));
    }

    @Face(login = false)
    @RequestMapping(value = "/fetchByGovCode", method = RequestMethod.POST)
    public @ResponseBody
    Object fetchByGovCode(ObjectFaceRequest<String> faceReq, Trace t) {
        String govCode = faceReq.getData();
        return areaManager.fetchByGovCode(t, govCode);
    }

    @Face(login = false)
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public @ResponseBody
    Object find(ListFaceRequest faceReq, Trace t) {
        WebQuery wq = faceReq.getQuery();
        if (wq != null) {
            return areaManager.find(t, wq.toQuery());
        }
        return null;
    }

    @Face(login = false)
    @RequestMapping(value = "/getChildrenCount", method = RequestMethod.POST)
    public @ResponseBody
    Object getChildrenCount(ParentIdAreaRequest faceReq, Trace t) {
        return areaManager.getChildrenCount(t, faceReq.getParentId(), faceReq.getStatusRule());
    }

    @Face(login = false)
    @RequestMapping(value = "/findChildren", method = RequestMethod.POST)
    public @ResponseBody
    Object findChildren(ParentCodeAreaRequest faceReq, Trace t) {
        return areaManager.findChildren(t, faceReq.getParentCode(), faceReq.getStatusRule());
    }

    @Face(login = false)
    @RequestMapping(value = "/findChildrenTreeNodesByParentCode", method = RequestMethod.POST)
    public @ResponseBody
    Object findChildrenTreeNodesByParentCode(ParentCodeAreaRequest faceReq, Trace t) {
        return areaManager.findChildrenTreeNodes(t, faceReq.getParentCode(), faceReq.getStatusRule());
    }

    @Face(login = false)
    @RequestMapping(value = "/findChildrenTreeNodesByParentId", method = RequestMethod.POST)
    public @ResponseBody
    Object findChildrenTreeNodesByParentId(ParentIdAreaRequest faceReq, Trace t) {
        return areaManager.findChildrenTreeNodes(t, faceReq.getParentId(), faceReq.getStatusRule());
    }

    @Face(login = false)
    @RequestMapping(value = "/findDirectChildren", method = RequestMethod.POST)
    public @ResponseBody
    Object findDirectChildren(ParentIdAreaRequest faceReq, Trace t) {
        return areaManager.findDirectChildren(t, faceReq.getParentId(), faceReq.getStatusRule());
    }

    @Face(login = false)
    @RequestMapping(value = "/findDirectChildrenTreeNodes", method = RequestMethod.POST)
    public @ResponseBody
    Object findDirectChildrenTreeNodes(ParentIdAreaRequest faceReq, Trace t) {
        return areaManager.findDirectChildrenTreeNodes(t, faceReq.getParentId(), faceReq.getStatusRule());
    }

    @Face(login = false)
    @RequestMapping(value = "/findByParentCodeAndLevel", method = RequestMethod.POST)
    public @ResponseBody
    Object findByParentCodeAndLevel(ParentCodeLevelAreaRequest faceReq, Trace t) {
        return areaManager.findByParentCodeAndLevel(t, faceReq.getParentCode(), faceReq.getLevelLt());
    }

}
