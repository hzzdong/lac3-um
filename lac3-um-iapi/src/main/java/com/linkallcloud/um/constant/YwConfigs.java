package com.linkallcloud.um.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.sys.YwSystemConfig;

public class YwConfigs {

	public static List<YwSystemConfig> defaultConfigs(Trace t, boolean rootCompany) {
		List<String> keys = null;
		if (rootCompany) {
			keys = Arrays.asList(Consts.CONFIG_CUSTOMER_MANAGE_MODE, Consts.CONFIG_ZZD, Consts.CONFIG_AREAS,
					Consts.CONFIG_LOGO);
		} else {
			keys = Arrays.asList(Consts.CONFIG_AREAS, Consts.CONFIG_LOGO);
		}

		List<YwSystemConfig> entities = new ArrayList<YwSystemConfig>();
		for (String key : keys) {
			entities.add(defaultConfig(t, key));
		}
		return entities;
	}

	public static YwSystemConfig defaultConfig(Trace t, String key) {
		if (!Strings.isBlank(key)) {
			if (Consts.CONFIG_CUSTOMER_MANAGE_MODE.equals(key)) {
				return new YwSystemConfig(Consts.CONFIG_CUSTOMER_MANAGE_MODE, "客户隔离模式", "org", "客户隔离模式", 10);
			} else if (Consts.CONFIG_ZZD.equals(key)) {
				return new YwSystemConfig(Consts.CONFIG_ZZD, "浙政钉对接", "no", "是否启用浙政钉对接", 20);
			} else if (Consts.CONFIG_AREAS.equals(key)) {
				return new YwSystemConfig(Consts.CONFIG_AREAS, "根区域", "", "可设置多个区域节点作为根区域", 30);
			} else if (Consts.CONFIG_LOGO.equals(key)) {
				return new YwSystemConfig(Consts.CONFIG_LOGO, "公司LOGO", "", "设置公司LOGO", 40);
			}
		}
		return null;
	}

}
