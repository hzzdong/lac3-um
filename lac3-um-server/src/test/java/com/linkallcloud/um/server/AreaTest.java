package com.linkallcloud.um.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.linkallcloud.um.service.sys.IAreaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.um.domain.sys.Area;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AreaTest {

	@Autowired
	private IAreaService areaService;

	private static Map<String, Area> AREAS = new HashMap<String, Area>();

	@Test
	public void importOfficialOrgs() throws Exception {
		Trace t = new Trace("AREA_2016_DATA");

		ClassLoader classLoader = AreaTest.class.getClassLoader();// getClass().getClassLoader();
		URL url = classLoader.getResource("com/linkallcloud/um/server/2016-area.txt");
		File file = new File(url.getFile());

		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			int sort = 100;
			String text = null;
			while ((text = bufferedReader.readLine()) != null) {
				System.out.println(text);
				saveArea(t, sort++, text);
			}
			bufferedReader.close();
		} catch (Exception e) {
		}

		Assert.assertTrue(true);
	}

	private void saveArea(Trace t, int srot, String text) {
		if (Strings.isBlank(text)) {
			return;
		}
		text = text.trim();
		if (Strings.isBlank(text) || text.length() < 8) {
			return;
		}

		if (text.charAt(0) >= '1' && text.charAt(0) <= '9') {
			//
		} else {
			text = text.substring(1);
		}

		String code6 = text.substring(0, 6);
		String name = text.substring(6).trim();
		if (code6.endsWith("0000")) {// 省级
			createLevel1Area(t, code6, name, srot);
		} else if (code6.endsWith("00")) {// 地市级
			createLevel2Area(t, code6, name, srot);
		} else {
			String l2Code = code6.substring(0, 4);
			if (AREAS.containsKey(l2Code)) {// 存在地市级，挂到地市下面.区县级
				createLevel3Area(t, code6, name, srot);
			} else {// 地市级
				createLevel2Area(t, code6, name, srot);
			}
		}
	}

	private void createLevel1Area(Trace t, String code6, String name, int srot) {
		String govCode = code6.substring(0, 2);
		Area area = new Area(0L, name, govCode, srot, 1);
		areaService.insert(t, area);
		AREAS.put(govCode, area);
	}

	private void createLevel2Area(Trace t, String code6, String name, int srot) {
		String l1Code = code6.substring(0, 2);
		Area l1Area = AREAS.get(l1Code);

		String govCode = code6;
		if (code6.endsWith("00")) {
			govCode = code6.substring(0, 4);
		}
		Area area = new Area(l1Area.getId(), name, govCode, srot, 2);
		areaService.insert(t, area);
		AREAS.put(govCode, area);
	}

	private void createLevel3Area(Trace t, String govCode, String name, int srot) {
		String l2Code = govCode.substring(0, 4);
		Area l2Area = AREAS.get(l2Code);

		Area area = new Area(l2Area.getId(), name, govCode, srot, 3);
		areaService.insert(t, area);
	}

}
