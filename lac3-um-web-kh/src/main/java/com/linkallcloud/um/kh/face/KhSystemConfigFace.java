package com.linkallcloud.um.kh.face;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.LacLog;
import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BizException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.face.message.response.ErrorFaceResponse;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.query.WebQuery;
import com.linkallcloud.core.query.rule.desc.StringRuleDescriptor;
import com.linkallcloud.um.constant.KhConfigs;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.dto.base.ConfigFaceRequest;
import com.linkallcloud.um.iapi.party.IKhCompanyManager;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.iapi.sys.IKhSystemConfigManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.face.base.BaseFace;
import com.linkallcloud.web.session.SessionUser;

@Controller
@RequestMapping(value = "/face/KhSystemConfig", method = RequestMethod.POST)
@Module(name = "系统配置")
public class KhSystemConfigFace extends BaseFace<KhSystemConfig, IKhSystemConfigManager> {

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhSystemConfigManager khSystemConfigManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAreaManager areaManager;

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhCompanyManager khCompanyManager;

	@Override
	protected IKhSystemConfigManager manager() {
		return khSystemConfigManager;
	}

	@Override
	protected List<KhSystemConfig> doFind(Trace t, WebQuery wq, SessionUser su) {
		if (wq != null) {
			if (wq.hasRule4Field("companyId")) {
				StringRuleDescriptor r = wq.getRule4Field("companyId");
				r.setData(su.companyId().toString());
			} else {
				wq.addRule(new StringRuleDescriptor("companyId", "eq", su.companyId(), "L"));
			}
		} else {
			wq = new WebQuery();
			wq.addRule(new StringRuleDescriptor("companyId", "eq", su.companyId(), "L"));
		}

		KhCompany company = khCompanyManager.fetchById(t, su.companyId());
		List<KhSystemConfig> entities = super.doFind(t, wq, su);
		if (entities == null || entities.size() <= 0) {
			return KhConfigs.defaultConfigs(t, company.isTopParent());
		} else {
			if ((company.isTopParent() && entities.size() >= 3) || (!company.isTopParent() && entities.size() >= 2)) {
				return entities;
			} else {
				List<KhSystemConfig> defs = KhConfigs.defaultConfigs(t,company.isTopParent());
				for (KhSystemConfig def : defs) {
					for (KhSystemConfig entity : entities) {
						if (def.getKey().equals(entity.getKey())) {
							def.setValue(entity.getValue());
							break;
						}
					}
				}
				return defs;
			}
		}
	}

	@LacLog(desc = "用户([(${su.sid.name})])修改了 [(${domainShowName})]信息([(${fr.orgId})], [(${fr.key})], [(${fr.value})]), TID:[(${tid})]")
	@Face(simple = true)
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public @ResponseBody Object changeStatus(ConfigFaceRequest fr, Trace t, SessionUser su) {
		if (!checkReferer(true)) {
			return new ErrorFaceResponse(Exceptions.CODE_ERROR_AUTH_PERMISSION, "Referer验证未通过");
		}
		Sid companyId = su.getCompany();
		if (fr.getOrgId() != null && !su.companyId().equals(fr.getOrgId()) && !Strings.isBlank(fr.getOrgUuid())) {
			KhCompany company = khCompanyManager.fetchByIdUuid(t, fr.getOrgId(), fr.getOrgUuid());
			if (company == null || !su.companyId().equals(company.getParentId())) {
				throw new BizException(Exceptions.CODE_ERROR_PARAMETER, "参数错误，或您无权执行此操作！");
			}
			companyId = new Sid(fr.getOrgId(), fr.getOrgUuid());
		}
		return manager().change(t, companyId, fr.getKey(), fr.getValue());
	}

}
