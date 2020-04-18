package com.linkallcloud.um.pc.controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IMenuManager;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

@Controller
public class IndexController {
	// private Log log = Logs.get();

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IMenuManager menuManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwUserManager ywUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String list(Trace t, ModelMap modelMap, SessionUser user) {
		modelMap.put("user", user);
		modelMap.put("userType", user.getUserType());

		// Application app = applicationManager.fetchByCode(t, "lac_app_um");
		Tree root = ywUserManager.getUserAppMenu(t, user.id(), user.appId());
		modelMap.put("items", root == null ? null : root.getChildren());

		String pwdStrength = (String) Controllers.getSessionObject("pwdStrength");
		modelMap.put("pwdStrength", pwdStrength);

		return "page/home";
	}

	@RequestMapping(value = "/getPermes", method = RequestMethod.GET)
	public @ResponseBody String[] getPermes(Trace t, SessionUser user) throws IllegalParameterException {
		// Application app = applicationManager.fetchByCode(t, "lac_app_um");
		String[] perms = ywUserManager.getUserAppPermissions4Menu(t, user.id(), user.appId());
		return perms;
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
}
