package com.linkallcloud.um.kh.face;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.LacLog;
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
import com.linkallcloud.um.domain.party.KhCompany;
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

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhRoleManager khRoleManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
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

	@LacLog(desc = "用户([(${su.sid.name})])更新了个人信息, TID:[(${tid})]")
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

	@LacLog(desc = "用户([(${su.sid.name})])更新其头像为[(${fr.data.value})], TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/updateHeaderImage", method = RequestMethod.POST)
	public @ResponseBody Object updateHeaderImage(ObjectFaceRequest<NameValue> fr, Trace t, SessionUser suser) {
		if (!checkReferer(true)) {
			return new ErrorFaceResponse(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		NameValue nv = fr.getData();
		return manager().updateHeaderImage(t, suser.getSid(), nv.getValue());
	}
	
	@LacLog(desc = "管理员([(${su.sid.name})])更新用户([(${fr.data.key})])头像为[(${fr.data.value})], TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/updateUserHeaderImage", method = RequestMethod.POST)
	public @ResponseBody Object updateUserHeaderImage(ObjectFaceRequest<NameValue> fr, Trace t, SessionUser suser) {
		if (!checkReferer(true)) {
			return new ErrorFaceResponse(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		NameValue nv = fr.getData();
		Long userId = Long.parseLong(nv.getKey());
		String userUuid = nv.getTitle();
		return manager().updateHeaderImage(t, new Sid(userId, userUuid), nv.getValue());
	}

	@LacLog(desc = "用户([(${su.sid.name})])修改了密码, TID:[(${tid})]")
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
		Long companyId = su.companyId();
		if (!page.hasRule4Field("companyId")) {
			page.addRule(new Equal("companyId", su.companyId()));
		} else {
			Equal companyIdRule = (Equal) page.getRule4Field("companyId");
			companyId = (Long) companyIdRule.getValue();
			KhCompany company = khCompanyManager.fetchById(t, companyId);
			if (company == null || !company.isChildOf(su.companyId())) {
				companyIdRule.setValue(-1L);
				companyId = -1L;
			}
		}

		if (page.hasRule4Field("parentId")) {// 查部门下的人
			page = manager().findUserPage4Org(t, page);
		} else {// 查整个公司的人
			if (su.isAdmin()) {
				page = manager().findUserPage4Org(t, page);
			} else {
				if (companyId.equals(su.companyId())) {
					page.addRule(new Equal("appId", su.appId()));
					page.addRule(new Equal("userId", su.id()));
					page = manager().findPermedUserPage(t, page);
				} else {
					page = manager().findUserPage4Org(t, page);
				}
			}
		}
		// page = manager().findPage(t, page);
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
		
		if (!Strings.isBlank(entity.getIco()) && entity.getIco().startsWith("http")) {
            entity.setIco(null);
        }
		super.doSave(t, entity, su);
	}

	@LacLog()
	@Face(simple = true)
	@RequestMapping(value = "/updateJz", method = RequestMethod.POST)
	public @ResponseBody Object updateJz(ObjectFaceRequest<KhUser> fr, Trace t, SessionUser su) {
		if (!checkReferer(true)) {
			return new ErrorFaceResponse(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		KhUser entity = fr.getData();
		entity.setJzCompanyId(su.companyId());
		manager().update(t, entity);
		return convert(t, "save", fr, entity);
	}

	@Face(simple = true)
	@RequestMapping(value = "/page4Role", method = RequestMethod.POST)
	public @ResponseBody Object page4Role(PageFaceRequest faceReq, Trace t, SessionUser su) {
		Page<KhUser> page = new Page<>(faceReq);

		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}
		
		Long companyId = su.companyId();
		Equal r = (Equal) page.getRule4Field("companyId");
		if (r != null) {
			r.setValue(companyId);
		} else {
			r = new Equal("companyId", companyId);
			page.addRule(r);
		}

		page = manager().findPage4Role(t, page);

		desensitization(page.getData());
		return page;
	}

	@Face(simple = true)
	@RequestMapping(value = "/page4UnRole", method = RequestMethod.POST)
	public @ResponseBody Object page4UnRole(PageFaceRequest faceReq, Trace t, SessionUser su) {
		Page<KhUser> page = new Page<>(faceReq);

		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Long companyId = su.companyId();
		Equal r = (Equal) page.getRule4Field("companyId");
		if (r != null) {
			r.setValue(companyId);
		} else {
			r = new Equal("companyId", companyId);
			page.addRule(r);
		}

		page = manager().findPage4UnRole(t, page);

		desensitization(page.getData());
		return page;
	}

	@Override
	protected Page<KhUser> doPage4Select(Trace t, Page<KhUser> page, SessionUser su) {
		Equal r = (Equal) page.getRule4Field("companyId");
		if (r != null) {
			Equal companyIdRule = (Equal) page.getRule4Field("companyId");
			Long companyId = (Long) companyIdRule.getValue();
			KhCompany company = khCompanyManager.fetchById(t, companyId);
			if (company == null || !company.isChildOf(su.companyId())) {
				r.setValue(-1L);
			}
		} else {
			page.addRule(new Equal("companyId", su.companyId()));
		}
		page = manager().findPage4Select(t, page);

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

	@LacLog(desc = "用户([(${su.sid.name})])添加 [(${domainShowName})]兼职人员([(${fr.id})]), TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/addPartTimeJob", method = RequestMethod.POST)
	public @ResponseBody Object addPartTimeJob(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		return khPartTimeJobManager.add(t, new Sid(fr.getId(), fr.getUuid()),
				new Sid(fr.getParentId(), fr.getParentUuid(), fr.getParentClass(), ""), fr.getRemark());
	}

	@LacLog(desc = "用户([(${su.sid.name})])删除 [(${domainShowName})]兼职人员([(${fr.id})]), TID:[(${tid})]")
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
