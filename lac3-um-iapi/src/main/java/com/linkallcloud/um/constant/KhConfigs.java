package com.linkallcloud.um.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.sys.KhSystemConfig;

public class KhConfigs {

	public static List<KhSystemConfig> defaultConfigs(Trace t, boolean rootCompany) {
		List<String> keys = null;
		if (rootCompany) {
			keys = Arrays.asList(Consts.CONFIG_AREAS, Consts.CONFIG_LOGO, Consts.CONFIG_ZZD);
		} else {
			keys = Arrays.asList(Consts.CONFIG_AREAS, Consts.CONFIG_LOGO);
		}

		List<KhSystemConfig> entities = new ArrayList<KhSystemConfig>();
		for (String key : keys) {
			entities.add(defaultConfig(t, key));
		}
		return entities;
	}

	public static KhSystemConfig defaultConfig(Trace t, String key) {
		if (!Strings.isBlank(key)) {
			if (Consts.CONFIG_AREAS.equals(key)) {
				return new KhSystemConfig(Consts.CONFIG_AREAS, "根区域", "", "可设置多个区域节点作为根区域", 10);
			} else if (Consts.CONFIG_LOGO.equals(key)) {
				return new KhSystemConfig(Consts.CONFIG_LOGO, "公司LOGO", "", "设置公司LOGO", 20);
			} else if (Consts.CONFIG_ZZD.equals(key)) {
				return new KhSystemConfig(Consts.CONFIG_ZZD, "浙政钉对接", "no", "是否启用浙政钉对接", 30);
			}
		}
		return null;
	}

}
