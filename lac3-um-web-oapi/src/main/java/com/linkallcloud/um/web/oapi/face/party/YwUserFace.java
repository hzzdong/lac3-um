package com.linkallcloud.um.web.oapi.face.party;

import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ListFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.exception.ArgException;
import com.linkallcloud.um.face.account.SessionUserRequest;
import com.linkallcloud.um.face.account.UserAppRequest;
import com.linkallcloud.um.iapi.party.IYwCompanyManager;
import com.linkallcloud.um.iapi.party.IYwDepartmentManager;
import com.linkallcloud.um.iapi.party.IYwUserManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.web.face.annotation.Face;

@Controller
@RequestMapping(value = "/face/YwUser", method = RequestMethod.POST)
@Module(name = "运维用户")
public class YwUserFace {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwUserManager ywUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwCompanyManager ywCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwDepartmentManager ywDepartmentManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAreaManager areaManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	@Face(login = false)
	@RequestMapping(value = "/fetchById", method = RequestMethod.POST)
	public @ResponseBody Object fetchById(IdFaceRequest faceReq, Trace t) {
		if (faceReq.getId() == null) {
			return null;
		}
		YwUser user = ywUserManager.fetchById(t, faceReq.getId());
		user.desensitization();
		return user;
	}

	@Face(login = false)
	@RequestMapping(value = "/fetchByGovCode", method = RequestMethod.POST)
	public @ResponseBody Object fetchByGovCode(ObjectFaceRequest<String> faceReq, Trace t) {
		String govCode = faceReq.getData();
		if (!Strings.isBlank(govCode)) {
			YwUser user = ywUserManager.fetchByGovCode(t, govCode);
			user.desensitization();
			return user;
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/fecthByAccount", method = RequestMethod.POST)
	public @ResponseBody Object fecthByAccount(ObjectFaceRequest<String> faceReq, Trace t) {
		String a = faceReq.getData();
		if (!Strings.isBlank(a)) {
			YwUser user = ywUserManager.fecthByAccount(t, a);
			user.desensitization();
			return user;
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findByMobile", method = RequestMethod.POST)
	public @ResponseBody Object findByMobile(ObjectFaceRequest<String> faceReq, Trace t) {
		String a = faceReq.getData();
		if (!Strings.isBlank(a)) {
			List<YwUser> users = ywUserManager.findByMobile(t, a);
			desensitization(users);
			return users;
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/assembleSessionUser", method = RequestMethod.POST)
	public @ResponseBody Object assembleSessionUser(SessionUserRequest faceReq, Trace t) {
		if (Strings.isBlank(faceReq.getAccount()) || Strings.isBlank(faceReq.getAppCode())) {
			throw new ArgException("Arg", "Account和AppCode都不能为空");
		}

		return ywUserManager.assembleSessionUser(t, faceReq.getAccount(), faceReq.getAppCode(),
				new Sid(faceReq.getCompanyId(), null, "YwCompany", null));
	}

	@Face(login = false)
	@RequestMapping(value = "/getUserAppMenu", method = RequestMethod.POST)
	public @ResponseBody Object getUserAppMenu(UserAppRequest faceReq, Trace t) {
		if (faceReq.getUserId() != null && faceReq.getAppId() != null) {
			Tree root = ywUserManager.getUserAppMenu(t, faceReq.getCompanyId(), faceReq.getUserId(),
					faceReq.getAppId());
			return root == null ? null : root.getChildren();
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/getUserAppMenuRes", method = RequestMethod.POST)
	public @ResponseBody Object getUserAppPermissions4Menu(UserAppRequest faceReq, Trace t) {
		if (faceReq.getUserId() != null && faceReq.getAppId() != null) {
			String[] perms = ywUserManager.getUserAppMenus(t, faceReq.getCompanyId(), faceReq.getUserId(),
					faceReq.getAppId());
			return perms;
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/getUserAppOrgIds", method = RequestMethod.POST)
	public @ResponseBody Object getUserAppOrgIds(UserAppRequest faceReq, Trace t) {
		if (faceReq.getUserId() != null && faceReq.getAppId() != null) {
			return ywUserManager.getUserAppOrgs(t, faceReq.getCompanyId(), faceReq.getUserId(), faceReq.getAppId());
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/getUserAppAreaIds", method = RequestMethod.POST)
	public @ResponseBody Object getUserAppAreaIds(UserAppRequest faceReq, Trace t) {
		if (faceReq.getUserId() != null && faceReq.getAppId() != null) {
			return ywUserManager.getUserAppAreas(t, faceReq.getCompanyId(), faceReq.getUserId(), faceReq.getAppId());
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public @ResponseBody Object find(ListFaceRequest faceReq, Trace t) {
		WebQuery wq = faceReq.getQuery();
		if (wq != null) {
			List<YwUser> users = ywUserManager.find(t, wq.toQuery());
			desensitization(users);
			return users;
		}
		return null;
	}

	@Face(login = false)
	@RequestMapping(value = "/findPage", method = RequestMethod.POST)
	public @ResponseBody Object findPage(PageFaceRequest faceReq, Trace t) {
		Page<YwUser> page = new Page<>(faceReq);
		page = ywUserManager.findPage(t, page);
		desensitization(page.getData());
		return page;
	}

	@Face(login = false)
	@RequestMapping(value = "/findPage4Select", method = RequestMethod.POST)
	public @ResponseBody Object findPage4Select(PageFaceRequest faceReq, Trace t) {
		Page<YwUser> page = new Page<>(faceReq);
		page = ywUserManager.findPage4Select(t, page);
		desensitization(page.getData());
		return page;
	}

	private void desensitization(List<YwUser> users) {
		if (users != null && users.size() > 0) {
			for (YwUser user : users) {
				user.desensitization();
			}
		}
	}

//	@Face(login = false)
//	@RequestMapping(value = "/findByRoleCompany", method = RequestMethod.POST)
//	public @ResponseBody Object findByRoleCompany(CompanyEntityRequest faceReq, Trace t) throws Exception {
//		if (faceReq.getCompanyId() == null || faceReq.getEntityId() == null) {
//			return null;
//		}
//		return ywUserManager.findByRoleCompany(t, faceReq.getCompanyId(), faceReq.getEntityId());
//	}

}
