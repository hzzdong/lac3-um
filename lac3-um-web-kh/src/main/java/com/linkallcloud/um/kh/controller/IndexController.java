package com.linkallcloud.um.kh.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
//import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.IllegalParameterException;
import com.linkallcloud.core.www.ISessionUser;
import com.linkallcloud.core.www.UrlPattern;
import com.linkallcloud.um.exception.AuthException;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;

@Controller
public class IndexController {

	@Value("${lac.lf.appcode}")
	private String myAppCode;

	@Value("${lac.lf.h5.home}")
	private String h5Home;

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
	public String vueAuthProxy(Trace t, HttpServletRequest request, ModelMap modelMap) {
		SessionUser user = Controllers.getSessionUser(myAppCode, request);
		// userType, loginName, userName, userId, companyId, companyName, validPeriod
		String token = Controllers.createToken(user.getUserType(), user.getLoginName(), user.name(), user.id(), user.companyId(),
				user.companyName(), 20);

		try {
			UrlPattern up = new UrlPattern(h5Home).append("token", token);
			return Controllers.redirect(up.url());
		} catch (UnsupportedEncodingException e) {
		}
		throw new AuthException();
	}
}
