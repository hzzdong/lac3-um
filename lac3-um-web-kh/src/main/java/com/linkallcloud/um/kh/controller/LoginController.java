package com.linkallcloud.um.kh.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.vo.LoginVo;
import com.linkallcloud.um.domain.sys.KhAccount;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.iapi.sys.IKhAccountManager;
import com.linkallcloud.web.exception.LoginException;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;
import com.linkallcloud.web.vc.SessionValidateCode;

@Controller
@RequestMapping
@Module(name = "用户登录")
public class LoginController {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhAccountManager khAccountManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhUserManager khUserManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhCompanyManager khCompanyManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhDepartmentManager khDepartmentManager;

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

    // @LacLog(db = true, desc = "用户([(${lvo.loginName})])登录系统,TID:[(${tid})].")
    @LacLog(desc = "用户([(${lvo.loginName})])登录系统,TID:[(${tid})].")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody Object postLcLogin(@RequestBody LoginVo lvo, HttpServletRequest request,
            HttpServletResponse response, HttpSession session, Trace t) {
        // SessionValidateCode svc = new SessionValidateCode(true);
        // if (!svc.validate(request, response, lvo.getVcode())) {
        // throw new LoginException(10002005, "验证码错误，请重新输入！");
        // }

        KhAccount account = khAccountManager.loginValidate(t, lvo.getLoginName(), Lang.md5(lvo.getPassword()));
        if (account != null) {
            SessionUser su = khUserManager.assembleSessionUser(t, lvo.getLoginName(), myAppCode, null);
            Controllers.login(myAppCode, su);
            String token = Controllers.createToken(su, 0);
            try {
                token = URLEncoder.encode(token, "UTF8");
            } catch (UnsupportedEncodingException e) {
            }
            return new Result<Object>(token);
            // setClientInfo2Cache(lvo.getLoginName(), lvo.getClient());
            // return WebUtils.makeSuccessResult(request.getContextPath() + getIndexUrl());
        }
        throw new LoginException("10002005", "账号或者密码错误，请重试！");
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public void getVerify(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        SessionValidateCode svc = new SessionValidateCode(true);
        svc.generate(request, response);
    }

    /**
     * 退出SSO登陆
     * 
     * @param modelMap
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout")
    public String logout(ModelMap modelMap, HttpSession session, HttpServletRequest request) {
        try {
            session.invalidate();
        } catch (Exception e) {
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(request.isSecure() ? "https://" : "http://");
        buffer.append(appServerName);
        buffer.append(request.getContextPath());
        buffer.append("/ssoauth");
        String service = buffer.toString();
        try {
            service = URLEncoder.encode(service, "UTF-8");
        } catch (Exception e) {
        }
        return Controllers.redirect(ssoServer + "/logout?from=" + myAppCode + "&service=" + service);
    }

}
