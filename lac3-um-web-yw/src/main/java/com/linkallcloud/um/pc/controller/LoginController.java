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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.enums.LoginMode;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.vo.LoginVo;
import com.linkallcloud.core.www.UrlPattern;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.YwAccount;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.exception.AuthException;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.iapi.sys.IYwAccountManager;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.session.SimpleSessionUser;
import com.linkallcloud.web.utils.Controllers;

@Controller
@RequestMapping
@Module(name = "用户登录")
public class LoginController {
	private static final Log log = Logs.get();

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwAccountManager ywAccountManager;

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

	@Value("${lac.lf.h5.home}")
	private String h5Home;

	@LacLog(desc = "用户([(${lvo.loginName})])登录系统,TID:[(${tid})].")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Object postLcLogin(@RequestBody LoginVo lvo, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, Trace t) {
		// SessionValidateCode svc = new SessionValidateCode(true);
		// if (!svc.validate(request, response, lvo.getVcode())) {
		// throw new LoginException(10002005, "验证码错误，请重新输入！");
		// }
		try {
			YwAccount account = ywAccountManager.loginValidate(t, lvo.getLoginName(), lvo.getPassword());
			if (account != null) {
				SessionUser su = ywUserManager.assembleSessionUser(t, lvo.getLoginName(), myAppCode, null);
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

	@RequestMapping(value = "/dwLogin", method = RequestMethod.GET)
	public String dwLogin(@RequestParam(value = "id") Long id, @RequestParam(value = "uuid") String uuid, Trace t,
			HttpSession session, HttpServletRequest request) {
		if (id == null || Strings.isBlank(uuid)) {
			throw new ArgException("您无法进行代维：参数错误。");
		}

		SessionUser srcUser = Controllers.getSessionUser(myAppCode, request);
		if (srcUser == null) {
			throw new AuthException("您无法进行代维，可能是能未处于登录状态。");
		}

		YwUser proxyUser = ywUserManager.fetchCompanyAdmin(t, new Sid(id, uuid));
		if (proxyUser == null) {
			throw new AuthException("您无法进行代维：参数错误，或本单位未设置管理员。");
		}

		SessionUser proxySu = ywUserManager.assembleSessionUser(t, proxyUser.getAccount(), myAppCode, null);

		try {
			session.invalidate();
		} catch (Exception e) {
		}

		proxySu.proxyFrom(srcUser);
		Controllers.login(myAppCode, proxySu);

		String token = Controllers.createToken(proxySu, 0);
		try {
			UrlPattern up = new UrlPattern(h5Home).append("token", token);
			return Controllers.redirect(up.url());
		} catch (UnsupportedEncodingException e) {
		}
		throw new AuthException("代维失败。");
	}

	@RequestMapping(value = "/dwLogout", method = RequestMethod.GET)
	public String dwLogout(Trace t, HttpSession session, HttpServletRequest request) {
		SessionUser proxySu = Controllers.getSessionUser(myAppCode, request);
		if (proxySu == null || !proxySu.getLoginMode().equals(LoginMode.Proxy.getCode())
				|| proxySu.getSrcUser() == null) {
			throw new AuthException("退出代维模式失败。");
		}

		SimpleSessionUser srcUser = (SimpleSessionUser) proxySu.getSrcUser();
		try {
			session.invalidate();
		} catch (Exception e) {
		}
		SessionUser suser = ywUserManager.assembleSessionUser(t, srcUser.getLoginName(), myAppCode,
				srcUser.getOrg());
		Controllers.login(myAppCode, suser);

		String token = Controllers.createToken(suser, 0);

		try {
			UrlPattern up = new UrlPattern(h5Home).append("token", token);
			return Controllers.redirect(up.url());
		} catch (UnsupportedEncodingException e) {
		}
		throw new AuthException("退出代维模式失败。");
	}

	@RequestMapping(value = "/orgLogin", method = RequestMethod.GET)
	public String dwLogout(@RequestParam(value = "orgId") Long orgId, @RequestParam(value = "orgType") String orgType,
			Trace t, HttpSession session, HttpServletRequest request) {
		SessionUser oldSu = Controllers.getSessionUser(myAppCode, request);
		if (oldSu == null) {
			throw new AuthException("操作失败。");
		}

		try {
			session.invalidate();
		} catch (Exception e) {
		}
		SessionUser suser = ywUserManager.assembleSessionUser(t, oldSu.getLoginName(), oldSu.appCode(),
				new Sid(orgId, null, orgType, null));
		Controllers.login(myAppCode, suser);

		String token = Controllers.createToken(suser, 0);

		try {
			UrlPattern up = new UrlPattern(h5Home).append("token", token);
			return Controllers.redirect(up.url());
		} catch (UnsupportedEncodingException e) {
		}
		throw new AuthException("退出代维模式失败。");
	}

}
