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
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.vo.LoginVo;
import com.linkallcloud.um.domain.party.Company;
import com.linkallcloud.um.domain.party.KhDepartment;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhDepartmentManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.sys.IAccountManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.web.exception.LoginException;
import com.linkallcloud.web.session.SessionUser;
import com.linkallcloud.web.utils.Controllers;
import com.linkallcloud.web.vc.SessionValidateCode;

@Controller
@RequestMapping
@Module(name = "用户登录")
public class LoginController {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAccountManager accountManager;

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
	public @ResponseBody Object postLcLogin(@RequestBody LoginVo lvo, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, Trace t) {
		// SessionValidateCode svc = new SessionValidateCode(true);
		// if (!svc.validate(request, response, lvo.getVcode())) {
		// throw new LoginException(10002005, "验证码错误，请重新输入！");
		// }

		Account account = accountManager.loginValidate(t, lvo.getLoginName(), Lang.md5(lvo.getPassword()));
		if (account != null) {
			SessionUser su = assembleSessionUser(t, account);
			Controllers.login(myAppCode, su);

//			// userType, loginName, userName, userId, companyId, companyName, validPeriod
			String token = Controllers.createToken("KhUser", lvo.getLoginName(), su.name(), su.id(), su.companyId(),
					su.companyName(), 60 * 10);

			try {
				token = URLEncoder.encode(token, "UTF8");
			} catch (UnsupportedEncodingException e) {
				return Exceptions.makeErrorResult(e);
			}

			return new Result<Object>(token);

			// setClientInfo2Cache(lvo.getLoginName(), lvo.getClient());
			// return WebUtils.makeSuccessResult(request.getContextPath() + getIndexUrl());
		}

		throw new LoginException("10002005", "账号或者密码错误，请重试！");
	}

	private SessionUser assembleSessionUser(Trace t, Account account) {
		User dbUser = khUserManager.fecthByAccount(t, account.getLoginname());
		Company dbCompany = khCompanyManager.fetchById(t, dbUser.getCompanyId());
		String orgName = dbCompany.getName();
		if (KhDepartment.class.getSimpleName().equals(dbUser.getParentClass())) {
			KhDepartment parent = khDepartmentManager.fetchById(t, dbUser.getParentId());
			if (parent != null) {
				orgName = parent.getName();
			}
		}
		SessionUser su = new SessionUser(dbUser.getId(), dbUser.getUuid(), dbUser.getAccount(), dbUser.getName(),
				dbUser.getUserType(), dbUser.getCompanyId(), dbCompany.getUuid(), dbCompany.getName(),
				dbUser.getParentId() != null ? dbUser.getParentId() : dbUser.getCompanyId(), orgName,
				dbUser.getClass().getSimpleName().substring(0, 2));
		su.setAdmin(dbUser.isAdmin());

		if (dbCompany.getAreaId() != null) {
			Area area = areaManager.fetchById(t, dbCompany.getAreaId());
			if (area != null) {
				su.setAreaInfo(area.getId(), area.getUuid(), area.getCode(), area.getName(), area.getLevel());
			}
		}

		Application app = applicationManager.fetchByCode(t, "lac_app_um_kh");
		String[] perms = khUserManager.getUserAppPermissions4Menu(t, su.id(), app.getId());
		su.setPermissions(perms, null, null);
		su.setAppInfo(app.getId(), app.getUuid(), app.getCode(), app.getName());
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
	@RequestMapping(value = "/exit")
	public String exit(ModelMap modelMap, HttpSession session, HttpServletRequest request) {
		try {
			session.invalidate();
		} catch (Exception e) {
		}
		return localLogin(session, request, modelMap);
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
