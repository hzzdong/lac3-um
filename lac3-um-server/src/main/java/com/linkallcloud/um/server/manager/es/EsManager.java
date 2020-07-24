package com.linkallcloud.um.server.manager.es;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.core.pagination.Page;
import com.linkallcloud.um.dto.es.EsResultDto;
import com.linkallcloud.um.dto.es.EsResultHits;
import com.linkallcloud.um.iapi.es.IEsManager;
import com.linkallcloud.um.service.es.IEsService;

@Service(interfaceClass = IEsManager.class, version = "${dubbo.service.version}")
@Component
@Module(name = "ES")
public class EsManager implements IEsManager<LacBusiLog> {

    @Autowired
    private IEsService esService;

    @Override
    public boolean deleteIndex(String index) {
        return esService.deleteIndex(index);
    }

    @Override
    public Page<LacBusiLog> find(Trace t, Page<LacBusiLog> page) {
        String index = (String) page.getCnds().get("index");
        String queryStr = (String) page.getCnds().get("queryStr");
        String indexStr = "";
        try {
            // 检查ES索引是否存在
            String[] indexs = index.split(",");
            List<String> reindexs = esService.getExistIndices(indexs);
            indexStr = String.join(",", reindexs);
            if (!"".equals(indexStr)) {
                String url = "/" + indexStr + "/_search?from=" + page.getStart() + "&size=" + page.getLength();

                String s = esService.find(url, queryStr);
                if (!StringUtils.isEmpty(s)) {
                    EsResultDto resultDto = JSON.parseObject(s, EsResultDto.class);
                    page.setRecordsTotal(resultDto.getRecordsTotal());
                    if (page.getRecordsTotal() > 0 && resultDto.getData() != null) {
                        List<LacBusiLog> data = new ArrayList<LacBusiLog>();
                        for (EsResultHits hits : resultDto.getData()) {
                            try {
                                LacBusiLog log = JSON.parseObject(hits.getSource(), LacBusiLog.class);
                                data.add(log);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println(JSON.toJSONString(data));
                        page.setData(data);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

}
