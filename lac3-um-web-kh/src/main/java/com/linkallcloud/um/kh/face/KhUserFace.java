package com.linkallcloud.um.kh.face;

import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.domain.party.KhRole;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.iapi.party.IKhRoleManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
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
	private IKhRoleManager khRoleManager;

	public KhUserFace() {
		super();
	}

	@Override
	protected IKhUserManager manager() {
		return khUserManager;
	}

	@Face(simple = true)
	@RequestMapping(value = "/fetchLoginUser", method = RequestMethod.POST)
	public @ResponseBody Object fetchLoginUser(ObjectFaceRequest<Object> fr, SessionUser suser) {
		return suser;
	}

	@Override
	protected Page<KhUser> doPage(Trace t, Page<KhUser> page, SessionUser su) {
		if (!page.hasRule4Field("companyId")) {
			page.addRule(new Equal("companyId", su.companyId()));
		}

		if (page.hasRule4Field("parentId")) {// 查部门下的人
			page = manager().findSelfUserPage(t, page);
		} else {// 查整个公司的人
			if (su.isAdmin()) {
				page = manager().findSelfUserPage(t, page);
			} else {
				page.addRule(new Equal("appId", su.appId()));
				page.addRule(new Equal("userId", su.id()));
				page = manager().findPermedSelfUserPage(t, page);
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
		super.doSave(t, entity, su);
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

		Equal rr = (Equal) page.getRule4Field("roleId");
		if (rr != null) {
			KhRole role = khRoleManager.fetchById(t, (Long) rr.getValue());
			page.addRule(new Equal("type", role.getType()));
		}

		page = manager().findPage4Role(t, page);

		desensitization(page.getData());
		return page;
	}

	@Override
	protected Page<KhUser> doPage4Select(Trace t, Page<KhUser> page, SessionUser su) {
		if (!page.hasRule4Field("roleId") || !page.hasRule4Field("roleUuid")) {
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "roleId,roleUuid参数错误。");
		}

		Equal r = (Equal) page.getRule4Field("companyId");
		if (r != null) {
			r.setValue(su.companyId());
		} else {
			page.addRule(new Equal("companyId", su.companyId()));
		}
		page = manager().findPage4Select(t, page);

		desensitization(page.getData());
		return page;
	}

}
