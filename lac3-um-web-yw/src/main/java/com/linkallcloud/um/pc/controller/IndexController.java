package com.linkallcloud.um.pc.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.core.www.UrlPattern;
import com.linkallcloud.um.exception.AuthException;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

@Controller
public class IndexController {
	// private Log log = Logs.get();

	@Value("${lac.lf.appcode}")
	private String myAppCode;

	@Value("${lac.lf.h5.home}")
	private String h5Home;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwUserManager ywUserManager;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String list(Trace t, HttpServletRequest request, ModelMap modelMap) {
		return "index";
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

}
