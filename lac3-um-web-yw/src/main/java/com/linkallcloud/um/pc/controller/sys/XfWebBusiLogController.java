package com.linkallcloud.um.pc.controller.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.linkallcloud.web.controller.BaseFullWebBusiLogController;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.um.domain.sys.UmWebLog;
import com.linkallcloud.um.excel.LogExport;
import com.linkallcloud.um.iapi.sys.IUmWebLogManager;
import com.linkallcloud.um.pc.utils.FileUtil;

@Controller
@RequestMapping(value = "/log", method = RequestMethod.POST)
public class XfWebBusiLogController extends BaseFullWebBusiLogController<UmWebLog, IUmWebLogManager> {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IUmWebLogManager lacWebBusiLogManager;

	@Override
	protected IUmWebLogManager manager() {
		return lacWebBusiLogManager;
	}

	@RequestMapping(value = "export", method = RequestMethod.POST)
	public void export(HttpServletResponse response,
			@RequestParam(value = "searchCnds", required = false) String searchCnds, Trace t) {
		String fileName = "操作日志_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".xls";
		Query query = JSON.parseObject(searchCnds, Query.class);
		List<UmWebLog> logs = manager().find(t, query);
		List<LogExport> list = new ArrayList<LogExport>();
		if (logs != null && logs.size() > 0) {
			for (UmWebLog blog : logs) {
				LogExport le = new LogExport(blog);
				list.add(le);
			}
		}
		FileUtil.exportExcel(list, null, "操作日志", LogExport.class, fileName, response);
	}

}
