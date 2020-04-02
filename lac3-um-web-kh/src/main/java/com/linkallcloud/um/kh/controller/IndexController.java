package com.linkallcloud.um.kh.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
//import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.core.www.UrlPattern;
import com.linkallcloud.um.exception.AuthException;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IMenuManager;
import com.linkallcloud.web.utils.Controllers;

@Controller
public class IndexController {
	// private Log log = Logs.get();

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IMenuManager menuManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	@Value("${lac.lf.appcode}")
	private String myAppCode;
	@Value("${lac.lf.h5.home}")
	private String h5Home;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String list(Trace t, HttpServletRequest request, ModelMap modelMap) {
		String ticket = request.getParameter("ticket");
		if (!Strings.isBlank(ticket)) {
			return "page/loginsuccess";
		}
		return home(t, request, modelMap);
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Trace t, HttpServletRequest request, ModelMap modelMap) {
		ISessionUser user = Controllers.getSessionUser(myAppCode, request);
		modelMap.put("user", user);
		modelMap.put("userType", user.getUserType());

		// Application app = applicationManager.fetchByCode(t, "lac_app_um_kh");
		Tree root = khUserManager.getUserAppMenu(t, Long.parseLong(user.getId()), Long.parseLong(user.getAppId()));
		modelMap.put("items", root == null ? null : root.getChildren());

		return "page/home";
	}

	@RequestMapping(value = "/getPermes", method = RequestMethod.GET)
	public @ResponseBody String[] getPermes(Trace t, HttpServletRequest request) throws IllegalParameterException {
		ISessionUser user = Controllers.getSessionUser(myAppCode, request);
		// Application app = applicationManager.fetchByCode(t, "lac_app_um_kh");
		String[] permedResCodes = khUserManager.getUserAppPermissions4Menu(t, Long.valueOf(user.getId()),
				Long.parseLong(user.getAppId()));
		return permedResCodes;
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String main(Trace t, ModelMap modelMap) {
		// ISessionUser user = Controllers.getSessionUser();
		return "page/welcome";
	}

	@RequestMapping(value = "/base", method = RequestMethod.GET)
	public String bg() {
		return "page/base :: global";
	}

	@RequestMapping(value = "/pub/noPermission", method = RequestMethod.GET)
	public String noPermission() {
		return "page/system/noPermission";
	}

	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public String help() {
		return "page/help";
	}

	@RequestMapping(value = "/fetchLoginUser", method = RequestMethod.GET)
	public @ResponseBody Result<Object> fetchLoginUser(Trace t, HttpServletRequest request)
			throws IllegalParameterException {
		ISessionUser user = Controllers.getSessionUser(myAppCode, request);
		return new Result<Object>(user);
	}

	@RequestMapping(value = "/vue", method = RequestMethod.GET)
	public String vueAuthProxy(Trace t, HttpServletRequest request, ModelMap modelMap) {
		ISessionUser user = Controllers.getSessionUser(myAppCode, request);
		// userType, loginName, userName, userId, companyId, companyName, validPeriod
		String token = Controllers.createToken("KhUser", user.getLoginName(), user.getName(),
				Long.parseLong(user.getId()), Long.parseLong(user.getCompanyId()), user.getCompanyName(), 20);

		try {
			UrlPattern up = new UrlPattern(h5Home).append("token", token);
			return Controllers.redirect(up.url());
		} catch (UnsupportedEncodingException e) {
		}
		throw new AuthException();
	}
}
