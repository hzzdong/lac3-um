package com.linkallcloud.um.server.service.es;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Multimap;
import com.linkallcloud.log.core.constant.LogMessageConstant;
import com.linkallcloud.um.service.es.IEsService;

import cn.hutool.core.util.IdUtil;

@Component
public class EsService implements IEsService {

    @Autowired
    private RestHighLevelClient highLevelClient;

    @Override
    public boolean instertBulk(Multimap<String, String> ds) {
        if (ds.size() > 0) {
            for (String k : ds.keys()) {
                Collection<String> vs = ds.get(k);
                if (vs != null && vs.size() > 0) {

                    String[] kk = k.split(LogMessageConstant.KEY_SEPARATOR);

                    StringBuffer sendStr = new StringBuffer();
                    for (String v : vs) {
                        String ent = "{\"index\":{\"_id\":\"" + IdUtil.simpleUUID() + "\"}} ";
                        sendStr.append(ent);
                        sendStr.append("\r\n");
                        sendStr.append(v);
                        sendStr.append("\r\n");
                    }

                    String endpoint = "/" + kk[0] + "/" + kk[1] + "/_bulk";
                    // System.out.println(endpoint);
                    Request request = new Request("PUT", endpoint);
                    request.setJsonEntity(sendStr.toString());
                    try {
                        Response response = highLevelClient.getLowLevelClient().performRequest(request);

                        // highLevelClient.getLowLevelClient().performRequestAsync(request, new ResponseListener() {
                        // @Override
                        // public void onSuccess(Response response) {
                        // System.out.println("ElasticSearch commit success!");
                        // }
                        //
                        // @Override
                        // public void onFailure(Exception e) {
                        // e.printStackTrace();
                        // System.out.println("ElasticSearch commit Failure!");
                        // }
                        // });
                        System.out.println(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> getExistIndices(String[] indices) {
        List<String> existIndexList = new ArrayList<String>();
        for (String index : indices) {
            try {
                index = index.trim().toLowerCase();
                Request request = new Request("HEAD", "/" + index + "");
                Response res = highLevelClient.getLowLevelClient().performRequest(request);
                if (res.getStatusLine().getStatusCode() == 200) {
                    existIndexList.add(index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return existIndexList;
    }

    @Override
    public boolean deleteIndex(String index) {
        List<String> ins = getExistIndices(new String[] { index });

        for (String inx : ins) {
            try {
                Request request = new Request("DELETE", "/" + inx + "");
                Response res = highLevelClient.getLowLevelClient().performRequest(request);
                if (res.getStatusLine().getStatusCode() == 200) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    @Override
    public String find(String url, String queryStr) {
        String reStr = "";
        StringEntity stringEntity = new StringEntity(queryStr, "utf-8");
        stringEntity.setContentType("application/json");
        Request request = new Request("GET", url);
        request.setEntity(stringEntity);
        try {
            Response res = highLevelClient.getLowLevelClient().performRequest(request);
            return EntityUtils.toString(res.getEntity(), "utf-8");
        } catch (Exception e) {
            reStr = "";
            e.printStackTrace();
        }

        return reStr;
    }

    @Override
    public String cat(String index) {
        String reStr = "";
        Request request = new Request("GET", "/_cat/indices/" + index + "?v");
        try {
            Response res = highLevelClient.getLowLevelClient().performRequest(request);

            InputStream inputStream = res.getEntity().getContent();
            byte[] bytes = new byte[0];
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String str = new String(bytes);
            reStr = str;
        } catch (Exception e) {
            e.printStackTrace();
            reStr = "";
        }
        return reStr;
    }

}
