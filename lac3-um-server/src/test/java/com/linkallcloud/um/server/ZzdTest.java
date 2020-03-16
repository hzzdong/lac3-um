package com.linkallcloud.um.server;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.www.utils.HttpClientFactory;

public class ZzdTest {

	private Log log = Logs.get();

//    private String GET_TOKEN_URL = "http://106.15.81.49:18004/oapi/getToken?appid=quanshengfuwuqiyebumen&appsecret=PDq8WARXY8GTR";
//    private String POST_ORG_CREATE = "http://106.15.81.49:18004/oapi/org/create?access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1NjM5NDUxODIzOTEsInVpZCI6InF1YW5zaGVuZ2Z1d3VxaXllYnVtZW4iLCJpYXQiOjE1NjM5Mzc5ODIzOTEsImNpZCI6ImRpbmd1c25memlzN25qdnlvZ2JpIn0.UZ99ZypzPUQAWBSESZKUFNvIuAQsFv1gG_dVS3K5fCU";
	private String GET_TOKEN_URL = "https://59.202.29.46:8004/oapi/getToken?appid=quanshengfuwuqiyebumen&appsecret=PDq8WARXY8GTR";
	private String POST_ORG_CREATE = "https://59.202.29.46:8004/oapi/org/create?access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHQiOjE1NjM5NDUxODIzOTEsInVpZCI6InF1YW5zaGVuZ2Z1d3VxaXllYnVtZW4iLCJpYXQiOjE1NjM5Mzc5ODIzOTEsImNpZCI6ImRpbmd1c25memlzN25qdnlvZ2JpIn0.UZ99ZypzPUQAWBSESZKUFNvIuAQsFv1gG_dVS3K5fCU";

	@Test
	public void getAccessTokenTest() throws Exception {
		String json = HttpClientFactory.me(false).get(GET_TOKEN_URL);// HttpUtil.getRequest(GET_TOKEN_URL);//
		log.infof("获取到的json格式的Token为:%s ", json);

	}

	@Test
	public void orgCreateTest() throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("name", "全省服务企业部门");
		params.put("parentId", 200011037L);
		params.put("type", 4);

		String json = HttpClientFactory.me(false).post(POST_ORG_CREATE, JSON.toJSONString(params));// HttpUtil.getRequest(GET_TOKEN_URL);//
		log.infof("获取到的json格式的Token为:%s ", json);
		// {"retCode":0,"retMessage":"success","retData":{"orgNumber":"200011247"}}

	}

}
