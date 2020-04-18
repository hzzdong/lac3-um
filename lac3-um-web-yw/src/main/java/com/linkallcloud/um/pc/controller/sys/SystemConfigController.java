package com.linkallcloud.um.pc.controller.sys;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Result;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.iapi.sys.IYwSystemConfigManager;

@Controller
@RequestMapping(value = "/System", method = RequestMethod.POST)
@Module(name = "系统配置")
public class SystemConfigController {

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IYwSystemConfigManager ywSystemConfigManager;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IAreaManager areaManager;

	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public String configArea(Trace t, ModelMap modelMap, AppVisitor av) {
		YwSystemConfig sc = ywSystemConfigManager.fetchByCompanyId(t, av.companyId());
		modelMap.put("sc", sc == null ? new YwSystemConfig() : sc);

		String areaName = "";
		if (sc != null && sc.getRootAreaId() != null && sc.getRootAreaId().longValue() > 0) {
			Area area = areaManager.fetchById(t, sc.getRootAreaId());
			areaName = area.getName();
		} else {
			areaName = "中华人民共和国";
		}
		modelMap.put("areaName", areaName);
		return "page/config/main";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> save(@RequestBody YwSystemConfig sc, Trace t, AppVisitor av) {
		sc.setCompanyId(av.companyId());
		ywSystemConfigManager.save(t, sc);
		return new Result<>(true);
	}

}
