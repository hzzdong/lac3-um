package com.linkallcloud.um.pc.controller;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.vo.LoginVo;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.party.YwDepartment;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IAccountManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;
import com.linkallcloud.web.vc.SessionValidateCode;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

@Controller
@RequestMapping
@Module(name = "用户登录")
public class LoginController {
    private static final Log log = Logs.get();

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IAccountManager accountManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IYwUserManager ywUserManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IYwCompanyManager ywCompanyManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IYwDepartmentManager ywDepartmentManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IApplicationManager applicationManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IAreaManager areaManager;

    @Value("${lac.lf.appcode}")
    private String myAppCode;

    @Value("${lac.lf.appServerName:localhost}")
    private String appServerName;

    @Value("${lac.lf.ssoServer:http://localhost/sso}")
    private String ssoServer;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String localLogin(HttpSession session, HttpServletRequest request, ModelMap modelMap) {
        SessionUser obj = (SessionUser) Controllers.getSessionUser(myAppCode);
        if (obj == null) {
            return "page/login";
        }
        return Controllers.redirect(getIndexUrl());
    }

    @WebLog(db = true, desc = "用户([(${lvo.loginName})])登录系统,TID:[(${tid})].")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    Result<String> postLcLogin(@RequestBody LoginVo lvo, HttpServletRequest request,
                               HttpServletResponse response, HttpSession session, Trace t) {
        // SessionValidateCode svc = new SessionValidateCode(true);
        // if (!svc.validate(request, response, lvo.getVcode())) {
        // throw new LoginException(10002005, "验证码错误，请重新输入！");
        // }

        try {
            Account account = accountManager.loginValidate(t, lvo.getLoginName(), lvo.getPassword());
            if (account != null) {
                User dbUser = ywUserManager.fecthByAccount(t, account.getLoginname());
                if (dbUser != null && dbUser.getStatus() == 0) {
                    SessionUser su = assembleSessionUser(t, dbUser);
                    Controllers.login(myAppCode, su);
                    Controllers.setSessionObject("pwdStrength", lvo.getPwdStrength());

                    // setClientInfo2Cache(lvo.getLoginName(), lvo.getClient());
                    // return WebUtils.makeSuccessResult(request.getContextPath() + getIndexUrl());
                    return new Result<String>(getIndexUrl());// request.getContextPath() +
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return new Result<String>("10002005", "账号或者密码错误，请重试！");
        }

        return new Result<String>("10002005", "账号或者密码错误，请重试！");
    }

    private SessionUser assembleSessionUser(Trace t, User dbUser) {
        Company dbCompany = ywCompanyManager.fetchById(t, dbUser.getCompanyId());
        String orgName = dbCompany.getName();
        if (YwDepartment.class.getSimpleName().equals(dbUser.getParentClass())) {
            YwDepartment parent = ywDepartmentManager.fetchById(t, dbUser.getParentId());
            if (parent != null) {
                orgName = parent.getName();
            }
        }
        SessionUser su = new SessionUser(String.valueOf(dbUser.getId()), dbUser.getUuid(), dbUser.getAccount(),
                dbUser.getName(), dbUser.getUserType(), dbUser.getCompanyId().toString(), dbCompany.getName(),
                dbUser.getParentId() != null ? dbUser.getParentId().toString() : dbUser.getCompanyId().toString(), orgName,
                dbUser.getClass().getSimpleName().substring(0, 2));
        su.setAdmin(dbUser.isAdmin());

        if (dbCompany.getAreaId() != null) {
            Area area = areaManager.fetchById(t, dbCompany.getAreaId());
            if (area != null) {
                su.setAreaInfo(area.getId(), area.getLevel(), area.getName());
            }
        }

        Application app = applicationManager.fetchByCode(t, "lac_app_um");
        String[] perms = ywUserManager.getUserAppPermissions4Menu(t, Long.valueOf(su.getId()), app.getId());
        su.setPermissions(perms, null, null);
        su.setAppInfo(app.getId().toString(), app.getUuid(), app.getCode(), app.getName());
        return su;
    }

    private String getIndexUrl() {
        return "/index";
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public void getVerify(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        SessionValidateCode svc = new SessionValidateCode(true);
        svc.generate(request, response);
    }

    /**
     * 退出本地登陆
     */
    @RequestMapping(value = "/exit", method = RequestMethod.GET)
    public String exit(ModelMap modelMap, HttpSession session, HttpServletRequest request) {
        try {
            session.invalidate();
        } catch (Exception e) {
        }
        return localLogin(session, request, modelMap);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(ModelMap modelMap, HttpSession session, HttpServletRequest request) {
        try {
            session.invalidate();
        } catch (Exception e) {
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(request.isSecure() ? "https://" : "http://");
        buffer.append(appServerName);
        buffer.append(request.getContextPath());
        buffer.append("/index");
        String service = buffer.toString();
        try {
            service = URLEncoder.encode(service, "UTF-8");
        } catch (Exception e) {
        }
        return Controllers.redirect(ssoServer + "/logout?from=" + myAppCode + "&service=" + service);
    }

}
