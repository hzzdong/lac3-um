package com.linkallcloud.um.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.constant.Consts;
import com.linkallcloud.um.domain.sys.YwSystemConfig;

public class YwConfigs {

	public static List<YwSystemConfig> defaultConfigs(Trace t) {
		List<String> keys = Arrays.asList(Consts.CONFIG_PERMISSION_ORG, Consts.CONFIG_PERMISSION_AREA,
				Consts.CONFIG_AREAS, Consts.CONFIG_LOGO, Consts.CONFIG_MANAGE_DEPARTMENT, Consts.CONFIG_ZZD);// ,Consts.CONFIG_COMPANY_CLASS
		List<YwSystemConfig> entities = new ArrayList<YwSystemConfig>();
		for (String key : keys) {
			entities.add(defaultConfig(t, key));
		}
		return entities;
	}

	public static YwSystemConfig defaultConfig(Trace t, String key) {
		if (!Strings.isBlank(key)) {
			if (Consts.CONFIG_PERMISSION_ORG.equals(key)) {
				return new YwSystemConfig(Consts.CONFIG_PERMISSION_ORG, "启用机构权限", "no", "是否启用机构权限功能");
			} else if (Consts.CONFIG_PERMISSION_AREA.equals(key)) {
				return new YwSystemConfig(Consts.CONFIG_PERMISSION_AREA, "启用区域权限", "no", "是否启用区域权限功能");
			} else if (Consts.CONFIG_MANAGE_DEPARTMENT.equals(key)) {
				return new YwSystemConfig(Consts.CONFIG_MANAGE_DEPARTMENT, "管理部门模式", "no", "是否启用管理部门模式");
			} else if (Consts.CONFIG_ZZD.equals(key)) {
				return new YwSystemConfig(Consts.CONFIG_ZZD, "浙政钉对接", "no", "是否启用浙政钉对接");
			} else if (Consts.CONFIG_AREAS.equals(key)) {
				return new YwSystemConfig(Consts.CONFIG_AREAS, "根区域", "", "可设置多个区域节点作为根区域");
			} else if (Consts.CONFIG_LOGO.equals(key)) {
				return new YwSystemConfig(Consts.CONFIG_LOGO, "公司LOGO", "", "设置公司LOGO");
			} 
//			else if (Consts.CONFIG_COMPANY_CLASS.equals(key)) {
//				return new YwSystemConfig(Consts.CONFIG_COMPANY_CLASS, "单位类型", "kh_xx", "请选择单位类型");
//			}
		}
		return null;
	}

}
