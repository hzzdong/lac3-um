package com.linkallcloud.um.web.oapi.face;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.sys.Account;
import com.linkallcloud.um.face.account.AccountIdWeChatRequest;
import com.linkallcloud.um.face.account.AccountValidateRequest;
import com.linkallcloud.um.face.account.AccountWeChatRequest;
import com.linkallcloud.um.face.account.ChangePasswordRequest;
import com.linkallcloud.um.face.account.ModifyPasswordRequest;
import com.linkallcloud.um.iapi.sys.IAccountManager;
import com.linkallcloud.web.face.annotation.Face;

public abstract class AccountFace<T extends Account, TM extends IAccountManager<T>> {

	protected abstract TM manager();

	@Face(login = false)
	@RequestMapping(value = "/fetchByAccount", method = RequestMethod.POST)
	public @ResponseBody Object fetchByAccount(ObjectFaceRequest<String> faceReq, Trace t) {
		String account = faceReq.getData();
		if (!Strings.isBlank(account)) {
			T ua = manager().fecthByAccount(t, account);
			ua.desensitization();
			return ua;
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/fetchByMobile", method = RequestMethod.POST)
	public @ResponseBody Object fetchByMobile(ObjectFaceRequest<String> faceReq, Trace t) {
		String m = faceReq.getData();
		if (!Strings.isBlank(m)) {
			T ua = manager().fecthByMobile(t, m);
			ua.desensitization();
			return ua;
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/fechByWechatOpenId", method = RequestMethod.POST)
	public @ResponseBody Object fechByWechatOpenId(AccountWeChatRequest faceReq, Trace t) {
		if (!Strings.isBlank(faceReq.getOpenid())) {
			T ua = manager().fechByWechatOpenId(t, faceReq.getOpenid());
			if(ua != null){
				ua.desensitization();
			}
			return ua;
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/updateAccountWechatOpenId", method = RequestMethod.POST)
	public @ResponseBody Object updateAccountWechatOpenId(AccountIdWeChatRequest faceReq, Trace t) {
		if (faceReq.getAccountId() != null && !Strings.isBlank(faceReq.getOpenid())) {
			return manager().updateAccountWechatOpenId(t, faceReq.getAccountId(), faceReq.getOpenid());
		}
		return false;
	}

	@Face(login = false)
	@RequestMapping(value = "/loginValidate", method = RequestMethod.POST)
	public @ResponseBody Object loginValidate(AccountValidateRequest faceReq, Trace t) {
		if (!Strings.isBlank(faceReq.getAccount()) && !Strings.isBlank(faceReq.getPassword())) {
			T ua = manager().loginValidate(t, faceReq.getAccount(), faceReq.getPassword());
			ua.desensitization();
			return ua;
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public @ResponseBody Object updatePassword(ChangePasswordRequest faceReq, Trace t) {
		if (faceReq.getId() != null && !Strings.isBlank(faceReq.getUuid()) && !Strings.isBlank(faceReq.getOldPwd())
				&& !Strings.isBlank(faceReq.getNewPwd())) {
			return manager().updatePassword(t, faceReq.getId(), faceReq.getUuid(), faceReq.getOldPwd(),
					faceReq.getNewPwd());
		}
		return false;
	}

	@Face(login = false)
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
	public @ResponseBody Object modifyPassword(ModifyPasswordRequest faceReq, Trace t) {
		if (!Strings.isBlank(faceReq.getAccount()) && !Strings.isBlank(faceReq.getOldPwd())
				&& !Strings.isBlank(faceReq.getNewPwd())) {
			return manager().modifyPassword(t, faceReq.getAccount(), faceReq.getOldPwd(), faceReq.getNewPwd());
		}
		return false;
	}

}
