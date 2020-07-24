package com.linkallcloud.um.pc.face;

import java.util.Date;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.PageFaceRequest;
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.iapi.es.IEsManager;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.web.session.SessionUser;

import cn.hutool.core.date.DateUtil;

@Controller
@RequestMapping(value = "/face/log", method = RequestMethod.POST)
@Module(name = "日志")
public class LogFace {

    @Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
    private IEsManager<LacBusiLog> esManager;

    @Face(simple = true)
    @RequestMapping(value = "/page", method = { RequestMethod.POST, RequestMethod.GET })
    public @ResponseBody Object page(PageFaceRequest faceReq, Trace t, SessionUser su) {
        Page<LacBusiLog> page = new Page<>(faceReq);

        String dateStr = "2020-07-23 00:00:00";
        Date date1 = DateUtil.parse(dateStr);
        String dateStr2 = "2020-07-23 23:59:59";
        Date date2 = DateUtil.parse(dateStr2);

        String queryStr = "{\n" + "    \"query\":{\n" + "        \"bool\":{\n" + "            \"must\":[\n"
                + "                {\n" + "                    \"range\":{\n" + "                        \"dtTime\":{\n"
                + "                            \"gte\":" + date1.getTime() + ",\n"
                + "                            \"lt\":" + date2.getTime() + "\n" + "                        }\n"
                + "                    }\n" + "                }\n" + "            ]\n" + "        }\n" + "    },\n"
                + "    \"aggs\":{\n" + "        \"aggCount\":{\n" + "            \"date_histogram\":{\n"
                + "                \"field\":\"dtTime\",\n" + "                \"interval\":3600000,\n"
                + "                \"min_doc_count\":0\n" + "            }\n" + "        }\n" + "    }\n" + "}";
        // esManager.deleteIndex("um_syslog_run_20200723");
        page.getCnds().put("index", "um_syslog_run_20200723");
        page.getCnds().put("queryStr", queryStr);
        page = esManager.find(t, page);

        //
        // page = doPage(t, page, su);
        // return convert(t, "page", faceReq, page);
        return page;
    }

}
