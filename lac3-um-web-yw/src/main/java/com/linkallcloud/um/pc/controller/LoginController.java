package com.linkallcloud.um.pc.controller;

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
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.vo.LoginVo;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IAccountManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

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

	@WebLog(db = true, desc = "用户([(${lvo.loginName})])登录系统,TID:[(${tid})].")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Object postLcLogin(@RequestBody LoginVo lvo, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, Trace t) {
		// SessionValidateCode svc = new SessionValidateCode(true);
		// if (!svc.validate(request, response, lvo.getVcode())) {
		// throw new LoginException(10002005, "验证码错误，请重新输入！");
		// }
		try {
			Account account = accountManager.loginValidate(t, lvo.getLoginName(), lvo.getPassword());
			if (account != null) {
				SessionUser su = ywUserManager.assembleSessionUser(t, lvo.getLoginName(), myAppCode);
				Controllers.login(myAppCode, su);
				String token = Controllers.createToken(su, 0);
				try {
					token = URLEncoder.encode(token, "UTF8");
				} catch (UnsupportedEncodingException e) {
				}

				return new Result<String>(token);
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return new Result<String>("10002005", "账号或者密码错误，请重试！");
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
