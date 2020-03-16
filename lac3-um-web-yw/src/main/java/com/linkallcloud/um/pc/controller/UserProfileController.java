package com.linkallcloud.um.pc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.web.utils.Controllers;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.iapi.party.IUserManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IAccountManager;
import com.linkallcloud.core.www.ISessionUser;

@Controller
@RequestMapping(value = "/uprofile", method = RequestMethod.POST)
@Module(name = "用户")
public class UserProfileController {

	@Reference(version = "${dubbo.service.version}")
	private IYwUserManager zfUserService;

	@Reference(version = "${dubbo.service.version}")
	private IAccountManager accountService;

	/**
	 * 个人设置
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String info(Trace t, ModelMap modelMap) {
		ISessionUser suser = Controllers.getSessionUser();
		if (suser != null && suser.getId() != null) {
			User dbUser = getConcreteUserServcie(suser.getUserType()).fetchById(t, Long.valueOf(suser.getId()));
			dbUser.setPassword(null);
			dbUser.setSalt(null);
			modelMap.put("user", dbUser);
		}
		return "page/sys/uprofile/info";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody <T extends User> boolean save(@RequestBody User user, Trace t) {
		ISessionUser suser = Controllers.getSessionUser();
		if (suser != null && suser.getId() != null) {
			T dbUser = (T) getConcreteUserServcie(suser.getUserType()).fetchById(t, Long.valueOf(suser.getId()));
			dbUser.setPassword(null);
			dbUser.setName(user.getName());
			dbUser.setIco(user.getIco());
			dbUser.setRemark(user.getRemark());
			dbUser.setNickName(user.getNickName());
			dbUser.setMobile(user.getMobile());
			dbUser.setEmail(user.getEmail());
			dbUser.setIdCard(user.getIdCard());
			dbUser.setSex(user.getSex());
			dbUser.setBirthday(user.getBirthday());
			((IUserManager<T>) getConcreteUserServcie(suser.getUserType())).update(t, dbUser);
		} else {
			throw new BaseRuntimeException("10002002", "您需要登录后才能使用此功能。");
		}
		return true;
	}

	@RequestMapping(value = "/changePwd", method = RequestMethod.GET)
	public String changePwd(ModelMap modelMap) {
		Controllers.putSessionUser2Model(modelMap);
		return "page/sys/uprofile/changePwd";
	}

	@RequestMapping(value = "modifyPwd", method = RequestMethod.POST)
	public @ResponseBody boolean modifyPwd(@RequestParam(value = "password") String password,
			@RequestParam(value = "newPwd") String newPwd, Trace t) {
		ISessionUser user = Controllers.getSessionUser();
		boolean result = false;
		if (user != null && !Strings.isBlank(password)) {
			Account account = accountService.fecthByAccount(t, user.getLoginName());
			result = accountService.updatePassword(t, account.getId(), account.getUuid(), password, newPwd);
		}
		if (!result) {
			throw new BaseRuntimeException("10002004");
		}
		return result;
	}

	private IUserManager<? extends User> getConcreteUserServcie(String type) {
		if (!Strings.isBlank(type)) {
			if ("YwUser".equals(type)) {
				return zfUserService;
			} else if ("KhUser".equals(type)) {
				//return qyUserService;
			}
		}
		throw new BaseRuntimeException("10002000", "用户类型错误，请联系管理员");
	}
}
