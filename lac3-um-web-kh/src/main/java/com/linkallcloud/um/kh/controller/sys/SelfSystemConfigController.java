package com.linkallcloud.um.kh.controller.sys;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.busilog.annotation.WebLog;
import com.linkallcloud.core.dto.AppVisitor;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.sys.Area;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.iapi.sys.IAreaManager;
import com.linkallcloud.um.iapi.sys.IKhSystemConfigManager;

@Controller
@RequestMapping(value = "/SelfSystem", method = RequestMethod.POST)
@Module(name = "系统配置")
public class SelfSystemConfigController {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IKhSystemConfigManager khSystemConfigManager;

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IAreaManager areaManager;

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public String configArea(Trace t, ModelMap modelMap, AppVisitor av) {
        KhSystemConfig sc = getKhSystemConfig(t, Long.parseLong(av.getCompanyId()));
        modelMap.put("sc", sc);
        return "page/config/main";
    }

    private KhSystemConfig getKhSystemConfig(Trace t, Long companyId) {
        KhSystemConfig sc = khSystemConfigManager.fetchByCompanyId(t, companyId);
        if (sc == null) {
            sc = new KhSystemConfig();
        }

        String areaName = "";
        if (sc != null && sc.getRootAreaId() != null && sc.getRootAreaId().longValue() > 0) {
            Area area = areaManager.fetchById(t, sc.getRootAreaId());
            areaName = area.getName();
        } else {
            areaName = "中华人民共和国";
        }
        sc.setRootAreaName(areaName);
        return sc;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @WebLog(db = true)
    public @ResponseBody Object save(@RequestBody KhSystemConfig sc, Trace t, AppVisitor av) {
        sc.setCompanyId(Long.parseLong(av.getCompanyId()));
        khSystemConfigManager.save(t, sc);
        return true;
    }

}
