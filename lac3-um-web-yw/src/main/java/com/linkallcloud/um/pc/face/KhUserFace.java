package com.linkallcloud.um.pc.face;

import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.NameValue;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.face.message.request.ParentIdFaceRequest;
import com.linkallcloud.core.face.message.request.PasswordFaceRequest;
import com.linkallcloud.core.face.message.response.ErrorFaceResponse;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.ptj.KhPartTimeJob;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.ptj.IKhPartTimeJobManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/KhUser", method = RequestMethod.POST)
@Module(name = "客户用户")
public class KhUserFace extends BaseFace<KhUser, IKhUserManager> {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhRoleManager khRoleManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhPartTimeJobManager khPartTimeJobManager;

	public KhUserFace() {
		super();
	}

	@Override
	protected IKhUserManager manager() {
		return khUserManager;
	}

	@Face(simple = true)
	@RequestMapping(value = "/fetchLoginUser", method = RequestMethod.POST)
	public @ResponseBody Object fetchLoginUser(ObjectFaceRequest<Object> fr, Trace t, SessionUser suser) {
		return suser;
	}

	@Face(simple = true)
	@RequestMapping(value = "/getLoginUser", method = RequestMethod.POST)
	public @ResponseBody Object getLoginUser(ObjectFaceRequest<Object> fr, Trace t, SessionUser suser) {
		return manager().fetchById(t, suser.id());
	}

	@WebLog(db = true, desc = "用户([(${su.sid.name})])更新了个人信息, TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/updateMe", method = RequestMethod.POST)
	public @ResponseBody Object updateMe(ObjectFaceRequest<KhUser> fr, Trace t, SessionUser su) {
		if (!checkReferer(true)) {
			return new ErrorFaceResponse(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		KhUser entity = fr.getData();
		entity.setId(su.id());
		entity.setUuid(su.uuid());
		entity.setIco(null);
		return manager().update(t, entity);
	}

	@WebLog(db = true, desc = "用户([(${su.sid.name})])更新其头像为[(${fr.data.value})], TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/updateHeaderImage", method = RequestMethod.POST)
	public @ResponseBody Object updateHeaderImage(ObjectFaceRequest<NameValue> fr, Trace t, SessionUser suser) {
		if (!checkReferer(true)) {
			return new ErrorFaceResponse(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		NameValue nv = fr.getData();
		return manager().updateHeaderImage(t, suser.getSid(), nv.getValue());
	}

	@WebLog(db = true, desc = "用户([(${su.sid.name})])修改了密码, TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/updatePass", method = RequestMethod.POST)
	public @ResponseBody Object updatePass(PasswordFaceRequest fr, Trace t, SessionUser su) {
		if (!checkReferer(true)) {
			return new ErrorFaceResponse(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		return manager().updatePassword(t, su.id(), su.uuid(), fr.getOldpass(), fr.getPassword());
	}

	@Override
	protected Page<KhUser> doPage(Trace t, Page<KhUser> page, SessionUser su) {
		page = manager().findUserPage4Org(t, page);
		desensitization(page.getData());
		return page;
	}

	private void desensitization(List<KhUser> users) {
		if (users != null && users.size() > 0) {
			for (KhUser user : users) {
				user.desensitization();
			}
		}
	}

	@Override
	protected void doSave(Trace t, KhUser entity, SessionUser su) {
		if (entity.getParentId() != null && entity.getParentId().longValue() < 0) {
			entity.setParentId(-1 * entity.getParentId());
		}

		if (entity.getParentId() != null && !Strings.isBlank(entity.getParentClass())
				&& entity.getParentClass().endsWith("Company")) {
			entity.setCompanyId(entity.getParentId());
		} else {
			entity.setCompanyId(su.companyId());
		}
		super.doSave(t, entity, su);
	}

	@Face(simple = true)
	@RequestMapping(value = "/page4Role4Yw", method = RequestMethod.POST)
	public @ResponseBody Object page4Role4Yw(PageFaceRequest faceReq, Trace t, SessionUser su) {
		Page<KhUser> page = new Page<>(faceReq);
		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}
		page = manager().page4Role4Yw(t, page);
		desensitization(page.getData());
		return page;
	}

	@Face(simple = true)
	@RequestMapping(value = "/page4UnRole4Yw", method = RequestMethod.POST)
	public @ResponseBody Object page4UnRole4Yw(PageFaceRequest faceReq, Trace t, SessionUser su) {
		Page<KhUser> page = new Page<>(faceReq);

		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}
		if (!page.hasRule4Field("companyId")) {
			page.addRule(new Equal("companyId", su.companyId()));
		}
		page = manager().page4UnRole4Yw(t, page);

		desensitization(page.getData());
		return page;
	}

	@Face(simple = true)
	@RequestMapping(value = "/page4PartTimeJob", method = RequestMethod.POST)
	public @ResponseBody Object page4PartTimeJob(PageFaceRequest faceReq, Trace t, SessionUser su) {
		Page<KhPartTimeJob> page = new Page<>(faceReq);
		if (!page.hasRule4Field("destOrgId") || !page.hasRule4Field("destOrgClass")) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "destOrgId,destOrgClass参数错误。");
		}
		page = khPartTimeJobManager.findPage(t, page);
		return page;
	}

	@WebLog(db = true, desc = "用户([(${su.sid.name})])添加 [(${domainShowName})]兼职人员([(${fr.id})]), TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/addPartTimeJob", method = RequestMethod.POST)
	public @ResponseBody Object addPartTimeJob(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		return khPartTimeJobManager.add(t, new Sid(fr.getId(), fr.getUuid()),
				new Sid(fr.getParentId(), fr.getParentUuid(), fr.getParentClass(), ""), fr.getRemark());
	}

	@WebLog(db = true, desc = "用户([(${su.sid.name})])删除 [(${domainShowName})]兼职人员([(${fr.id})]), TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/removePartTimeJob", method = RequestMethod.POST)
	public @ResponseBody Object removePartTimeJob(IdFaceRequest fr, Trace t, SessionUser suser) {
		return khPartTimeJobManager.delete(t, fr.getId(), fr.getUuid());
	}

	@Face(simple = true)
	@RequestMapping(value = "/page4PartTimeJobOfUser", method = RequestMethod.POST)
	public @ResponseBody Object page4PartTimeJobOfUser(PageFaceRequest faceReq, Trace t, SessionUser su) {
		Page<KhPartTimeJob> page = new Page<>(faceReq);
		if (!page.hasRule4Field("userId") || !page.hasRule4Field("userUuid")) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "userId,userUuid参数错误。");
		}
		page = khPartTimeJobManager.findPage(t, page);
		return page;
	}
}
