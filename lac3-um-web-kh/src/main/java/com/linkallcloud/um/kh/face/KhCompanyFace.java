package com.linkallcloud.um.kh.face;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.face.message.request.ParentIdFaceRequest;
import com.linkallcloud.core.face.message.request.RelFaceRequest;
import com.linkallcloud.core.face.message.request.RelParentIdFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.constant.Consts;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.party.KhUser;
import com.linkallcloud.um.domain.party.Rel4OrgLeader;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.iapi.sys.IApplicationManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseTreeFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/KhCompany", method = RequestMethod.POST)
@Module(name = "客户单位")
public class KhCompanyFace extends BaseTreeFace<KhCompany, IKhCompanyManager> {

	@Value("${oapi.appcode}")
	private String myAppCode;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	@Override
	protected IKhCompanyManager manager() {
		return khCompanyManager;
	}

	/**
	 * 加载单位组织树，含单位和部门节点。 参见：com.linkallcloud.um.constant.Consts
	 * （1）若type='SelfTree'，则组织树中紧包含本单位的部门节点；
	 * （2）若type='CompanyTree'，则组织树中包含本单位的部门节点及包含子单位的根节点；
	 * （3）若type='FullTree'，则组织树中包含本单位的部门节点及包含子单位的所有节点；
	 * （4）若type="FullTreeCompany"，则组织树中包含所有子单位节点，不含部门节点
	 * 
	 * @param fr
	 * @param t
	 * @param su
	 * @return
	 */
	@Face(simple = true)
	@RequestMapping(value = "/loadCompanyTree", method = RequestMethod.POST)
	public @ResponseBody Object loadCompanyTree(IdFaceRequest fr, Trace t, SessionUser su) {
		Sid company = su.getCompany();
		if (fr.getId() != null && !Strings.isBlank(fr.getUuid())) {
			company = new Sid(fr.getId(), fr.getUuid());
		}
		Tree root = khCompanyManager.getCompanyTree(t, fr.getType(), company);
		List<Tree> items = Arrays.asList(root);
		return items;
	}

//	/**
//	 * 加载当前登录用户有权限的组织树（子单位紧紧包含子单位的根节点）。
//	 * 
//	 * @param fr
//	 * @param t
//	 * @param su
//	 * @return
//	 */
//	@Face(simple = true)
//	@RequestMapping(value = "/loadTree", method = RequestMethod.POST)
//	public @ResponseBody Object loadKhCompanyTree(ObjectFaceRequest<Object> fr, Trace t, SessionUser su) {
//		// Application app = applicationManager.fetchByCode(t, myAppCode);
//		Tree root = khCompanyManager.getPermedCompanyTree(t, su.appId(), su.id());
//		if (root != null && "v-root".equals(root.getId())) {
//			return root.getChildren();
//		} else {
//			List<Tree> items = Arrays.asList(root);
//			return items;
//		}
//	}

	/**
	 * 加载当前登录用户有权限的组织树（子单位紧紧包含子单位的根节点）。
	 */
	@Override
	public List<Tree> doLoadTree(Trace t, IdFaceRequest fr, SessionUser su) {
		// Application app = applicationManager.fetchByCode(t, myAppCode);
		Tree root = khCompanyManager.getPermedCompanyTree(t, su.appId(), su.id());
		if (root != null && "v-root".equals(root.getId())) {
			return root.getChildren();
		} else {
			List<Tree> items = Arrays.asList(root);
			return items;
		}
	}

	/**
	 * 加载单位完整的组织树，含单位和部门节点
	 * 
	 * @param fr
	 * @param t
	 * @param su
	 * @return
	 */
	@Face(simple = true)
	@RequestMapping(value = "/loadFullTree", method = RequestMethod.POST)
	public @ResponseBody Object loadKhCompanyFullTree(IdFaceRequest fr, Trace t, SessionUser su) {
		Sid company = su.getCompany();
		if (fr.getId() != null && !Strings.isBlank(fr.getUuid())) {
			company = new Sid(fr.getId(), fr.getUuid());
		}
		Tree root = khCompanyManager.getCompanyTree(t, Consts.ORG_TREE_TYPE_FULL, company);
		List<Tree> items = Arrays.asList(root);
		return items;
	}

	/**
	 * 加载单位完整的组织树，含单位节点，不含部门节点
	 * 
	 * @param fr
	 * @param t
	 * @param su
	 * @return
	 */
	@Face(simple = true)
	@RequestMapping(value = "/loadTreeOfCompany", method = RequestMethod.POST)
	public @ResponseBody Object loadTreeOfCompany(IdFaceRequest fr, Trace t, SessionUser su) {
		Sid company = su.getCompany();
		if (fr.getId() != null && !Strings.isBlank(fr.getUuid())) {
			company = new Sid(fr.getId(), fr.getUuid());
		}
		Tree root = khCompanyManager.getCompanyTree(t, Consts.ORG_TREE_TYPE_FULL_COMPANY, company);
		return root.getChildren();
	}

	@Override
	protected void doSave(Trace t, KhCompany entity, SessionUser su) {
		entity.setParentId(su.companyId());
		entity.setParentClass(KhCompany.class.getSimpleName());
		entity.setIco(null);
		super.doSave(t, entity, su);
	}

	@Override
	protected Object convert(Trace t, String method, FaceRequest fr, KhCompany entity) {
		if (!Strings.isBlank(method) && method.equals("save")) {
			Tree node = entity.toTreeNode();
			node.setId("-" + node.getId());
			node.setpId("-" + node.getpId());
			return node;
		}
		return entity;
	}

	@Face(simple = true)
	@RequestMapping(value = "/loadCompanyAreaFullTree", method = RequestMethod.POST)
	public @ResponseBody Object loadCompanyAreaFullTree(ObjectFaceRequest<Object> fr, Trace t, SessionUser su) {
		Tree root = manager().loadCompanyAreaFullTree(t, su.getCompany());
		return root.getChildren();
	}

	@Face(simple = true)
	@RequestMapping(value = "/loadCompanyAreaTree", method = RequestMethod.POST)
	public @ResponseBody Object loadCompanyAreaTree(ObjectFaceRequest<Object> fr, Trace t, SessionUser su) {
		Tree root = manager().loadCompanyAreaTree(t, su.getCompany());
		return root.getChildren();
	}

	@Face(simple = true)
	@RequestMapping(value = "/getConfigCompanyAreaRoots", method = RequestMethod.POST)
	public @ResponseBody Object getConfigCompanyAreaRoots(IdFaceRequest fr, Trace t, SessionUser su) {
//		Sid companyId = su.getCompany();
//		if (fr.getId() != null) {
//			companyId = new Sid(fr.getId(), fr.getUuid());
//		}
		Long companyId = fr.getId();
		if (companyId == null || companyId.longValue() <= 0) {
			companyId = su.companyId();
		}
		return manager().getConfigCompanyAreaRoots(t, companyId);
	}

	@Face(simple = true)
	@RequestMapping(value = "/getConfigCompanyAreaRootIds", method = RequestMethod.POST)
	public @ResponseBody Object getConfigCompanyAreaRootIds(IdFaceRequest fr, Trace t, SessionUser su) {
//		Sid companyId = su.getCompany();
//		if (fr.getId() != null) {
//			companyId = new Sid(fr.getId(), fr.getUuid());
//		}
		Long companyId = fr.getId();
		if (companyId == null || companyId.longValue() <= 0) {
			companyId = su.companyId();
		}
		return manager().getConfigCompanyAreaRootIds(t, companyId);
	}

	@WebLog(db = true, desc = "用户([(${su.sid.name})])添加 [(${domainShowName})]应用许可, TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/addApps", method = RequestMethod.POST)
	public @ResponseBody Object addApps(RelFaceRequest fr, Trace t, SessionUser su) {
		if (fr.getId().equals(su.companyId())) {// 不能给自己公司许可应用
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误，或您无权执行此操作！");
		}
		return manager().addApps(t, fr.getId(), fr.getUuid(), fr.getUuidIds());
	}

	@WebLog(db = true, desc = "用户([(${su.sid.name})])删除了 [(${domainShowName})]应用许可([(${fr.id})]), TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/removeApp", method = RequestMethod.POST)
	public @ResponseBody Object removeApp(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		if (fr.getParentId().equals(suser.companyId())) {// 不能给自己公司许可应用
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误，或您无权执行此操作！");
		}
		Map<String, Long> appUuidIds = new HashMap<String, Long>();
		appUuidIds.put(fr.getUuid(), fr.getId());
		return manager().removeApps(t, fr.getParentId(), fr.getParentUuid(), appUuidIds);
	}

	@Face(simple = true)
	@RequestMapping(value = "/getPermedAppMenuTree", method = RequestMethod.POST)
	public @ResponseBody Object getPermedAppMenuTree(ParentIdFaceRequest fr, Trace t, SessionUser suser) {
		Sid forCompany = new Sid(fr.getParentId(), fr.getParentUuid());
		Sid app = new Sid(fr.getId(), fr.getUuid());
		Tree tree = manager().findPermedAppMenusTree(t, suser.getCompany(), forCompany, app);
		return tree.getChildren();
	}

	@WebLog(db = true, desc = "用户([(${su.sid.name})])修改了 [(${domainShowName})]应用菜单权限([(${fr.parentId})], [(${fr.id})]), TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/saveAppMenuPerm", method = RequestMethod.POST)
	public @ResponseBody Object saveAppMenuPerm(RelParentIdFaceRequest fr, Trace t, SessionUser suser) {
		Sid companyId = new Sid(fr.getParentId(), fr.getParentUuid());
		Sid appId = new Sid(fr.getId(), fr.getUuid());
		return manager().saveAppMenuPerm(t, companyId, appId, fr.getUuidIds());
	}

	@Face(simple = true)
	@RequestMapping(value = "/leaderPage", method = RequestMethod.POST)
	public @ResponseBody Object page(PageFaceRequest pfr, Trace t, AppVisitor av) {
		Page<KhUser> page = new Page<KhUser>(pfr);
		return manager().leaderPage(t, page);
	}

	@Face(simple = true)
	@WebLog(db = true, desc = "用户([(${su.sid.name})])添加 [(${domainShowName})]机构领导([(${fr.data.userId})]), TID:[(${tid})]")
	@RequestMapping(value = "/addLeader", method = RequestMethod.POST)
	public @ResponseBody Object addLeaders(ObjectFaceRequest<Rel4OrgLeader> fr, Trace t) {
		Rel4OrgLeader leader = fr.getData();
		return manager().addLeader(t, leader);
	}

	@Face(simple = true)
	@WebLog(db = true, desc = "用户([(${su.sid.name})])删除 [(${domainShowName})]的机构领导([(${fr.data.userId})]), TID:[(${tid})]")
	@RequestMapping(value = "/deleteLeader", method = RequestMethod.POST)
	public @ResponseBody Object deleteLeaders(ObjectFaceRequest<Rel4OrgLeader> fr, Trace t) {
		Rel4OrgLeader leader = fr.getData();
		return manager().deleteLeader(t, leader);
	}

	@Face(simple = true)
	@RequestMapping(value = "/fetchMyCompany", method = RequestMethod.POST)
	public @ResponseBody Object fetchMyCompany(ObjectFaceRequest<String> faceReq, Trace t, SessionUser su) {
		Long myCompanyId = su.companyId();
		if (myCompanyId == null) {
			KhUser user = khUserManager.fetchById(t, su.id());
			myCompanyId = user.getCompanyId();
		}
		KhCompany entity = manager().fetchById(t, myCompanyId);
		return entity;
	}

}
