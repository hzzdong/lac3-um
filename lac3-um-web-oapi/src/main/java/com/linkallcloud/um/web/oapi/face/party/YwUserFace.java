package com.linkallcloud.um.web.oapi.face.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.web.face.annotation.Face;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.ptj.YwPartTimeJob;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.ptj.IYwPartTimeJobManager;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/face/YwUser", method = RequestMethod.POST)
@Module(name = "运维用户")
public class YwUserFace extends UserFace<YwUser, IYwUserManager, YwPartTimeJob, IYwPartTimeJobManager> {

    @DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IYwUserManager ywUserManager;

    @DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IYwPartTimeJobManager ywPartTimeJobManager;

    @Override
    protected IYwUserManager userManager() {
        return ywUserManager;
    }

    @Override
    protected IYwPartTimeJobManager ptManager() {
        return ywPartTimeJobManager;
    }

    @Face(login = false)
    @RequestMapping(value = "/fetchByUuid", method = RequestMethod.POST)
    public @ResponseBody
    Object fetchByUuid(IdFaceRequest faceReq, Trace t) {
        if (faceReq.getUuid() == null) {
            return null;
        }
        YwUser user = userManager().fetchByUuid(t, faceReq.getUuid());
        user.desensitization();
        return user;
    }


    @Face(login = false)
    @RequestMapping(value = "/find4CompanyAndDepartment", method = RequestMethod.POST)
    public @ResponseBody Object find4CompanyAndDepartment(ListFaceRequest faceReq, Trace t) {
        WebQuery wq = faceReq.getQuery();
        if (wq != null) {
            List<YwUser> users = userManager().find4CompanyAndDepartment(t, wq.toQuery());
            desensitization(users);
            return users;
        }
        return null;
    }

}
