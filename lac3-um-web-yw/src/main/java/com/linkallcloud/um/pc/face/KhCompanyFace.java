package com.linkallcloud.um.pc.face;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.request.ParentIdFaceRequest;
import com.linkallcloud.core.face.message.request.RelFaceRequest;
import com.linkallcloud.core.face.message.request.RelParentIdFaceRequest;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.um.constant.Consts;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.sys.IYwSystemConfigManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/KhCompany", method = RequestMethod.POST)
@Module(name = "客户单位")
public class KhCompanyFace extends BaseFace<KhCompany, IKhCompanyManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;
	
	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwSystemConfigManager ywSystemConfigManager;

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

	// 只查询顶层公司
	@Override
	protected Page<KhCompany> doPage(Trace t, Page<KhCompany> page, SessionUser su) {
		page.addRule(new Equal("top", true));
		page.addRule(new Equal("ywUserId", su.id()));
		page.addRule(new Equal("companyId", su.companyId()));
		page.addRule(new Equal("appId", su.appId()));
		return super.doPage(t, page, su);
	}

	@Override
	protected void doSave(Trace t, KhCompany entity, SessionUser su) {
		entity.setYwCompanyId(su.companyId());
		entity.setCreateUserId(su.id());
		
		entity.setParentId(null);
		entity.setParentClass(null);
		
		entity.setIco(null);
		
		super.doSave(t, entity, su);
	}

	@LacLog(desc = "用户([(${su.sid.name})])添加 [(${domainShowName})]应用许可, TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/addApps", method = RequestMethod.POST)
	public @ResponseBody Object addApps(RelFaceRequest fr, Trace t, SessionUser su) {
		if (fr.getId().equals(su.companyId())) {// 不能给自己公司许可应用
			throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误，或您无权执行此操作！");
		}
		return manager().addApps(t, fr.getId(), fr.getUuid(), fr.getUuidIds());
	}

	@LacLog(desc = "用户([(${su.sid.name})])删除了 [(${domainShowName})]应用许可([(${fr.id})]), TID:[(${tid})]")
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
		Tree tree = manager().findPermedAppMenusTree(t, null, forCompany, app);
		return tree.getChildren();
	}

	@LacLog(desc = "用户([(${su.sid.name})])修改了 [(${domainShowName})]应用菜单权限([(${fr.parentId})], [(${fr.id})]), TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/saveAppMenuPerm", method = RequestMethod.POST)
	public @ResponseBody Object saveAppMenuPerm(RelParentIdFaceRequest fr, Trace t, SessionUser suser) {
		Sid companyId = new Sid(fr.getParentId(), fr.getParentUuid());
		Sid appId = new Sid(fr.getId(), fr.getUuid());
		return manager().saveAppMenuPerm(t, companyId, appId, fr.getUuidIds());
	}

}
