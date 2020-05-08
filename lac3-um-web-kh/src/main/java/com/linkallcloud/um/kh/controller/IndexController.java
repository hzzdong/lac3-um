package com.linkallcloud.um.kh.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
//import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.enums.LoginMode;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.core.www.UrlPattern;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.exception.AuthException;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

@Controller
public class IndexController {

	@Value("${lac.lf.appcode}")
	private String myAppCode;

	@Value("${lac.lf.h5.home}")
	private String h5Home;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String list(Trace t, HttpServletRequest request, ModelMap modelMap) {
		return "page/home";
	}

	@RequestMapping(value = "/fetchLoginUser", method = RequestMethod.GET)
	public @ResponseBody Result<Object> fetchLoginUser(Trace t, HttpServletRequest request)
			throws IllegalParameterException {
		ISessionUser user = Controllers.getSessionUser(myAppCode, request);
		return new Result<Object>(user);
	}

	@RequestMapping(value = "/ssoauth", method = RequestMethod.GET)
	public String vueAuthProxy(@RequestParam(value = "redirect", required = false) String redirect, Trace t,
			HttpServletRequest request, ModelMap modelMap) {
		SessionUser user = Controllers.getSessionUser(myAppCode, request);
		String token = Controllers.createToken(user, 0);

		try {
			String h5Url = h5Home;
			if (!Strings.isBlank(redirect)) {
				h5Url += redirect;
			}
			UrlPattern up = new UrlPattern(h5Url).append("token", token);
			return Controllers.redirect(up.url());
		} catch (UnsupportedEncodingException e) {
		}
		throw new AuthException();
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

		KhUser proxyUser = khUserManager.fetchCompanyAdmin(t, new Sid(id, uuid));
		if (proxyUser == null) {
			throw new AuthException("您无法进行代维：参数错误，或本单位未设置管理员。");
		}

		SessionUser proxySu = khUserManager.assembleSessionUser(t, proxyUser.getAccount(), myAppCode);

		try {
			session.invalidate();
		} catch (Exception e) {
		}

		proxySu.proxyFrom(srcUser);
		Controllers.login(myAppCode, proxySu);

		// userType, loginName, userName, userId, companyId, companyName, validPeriod
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

		SessionUser srcUser = proxySu.getSrcUser();
		try {
			session.invalidate();
		} catch (Exception e) {
		}
		Controllers.login(myAppCode, srcUser);

		String token = Controllers.createToken(srcUser, 0);

		try {
			UrlPattern up = new UrlPattern(h5Home).append("token", token);
			return Controllers.redirect(up.url());
		} catch (UnsupportedEncodingException e) {
		}
		throw new AuthException("退出代维模式失败。");
	}
}
