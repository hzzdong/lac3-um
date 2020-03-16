package com.linkallcloud.um.pc.oapi.dd;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.um.oapi.dd.dto.DdFaceRequest;
import com.linkallcloud.um.pc.oapi.BaseOapi;
import com.linkallcloud.core.www.utils.HttpClientFactory;

@Component
public class ZzdUserOapi extends BaseOapi {

	public String getAccessToken() {
		String sendMsgPkg = packMessage(null);
		String responseJson = HttpClientFactory.me(false).post(umOapiUrl + "/face/Dd/getAccessToken", sendMsgPkg);
		String token = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<String>>() {
		});
		return token;
	}

	public Long getSingleUserIdByMobile(String accessToken, String mobile) {
		DdFaceRequest<String> faceReq = new DdFaceRequest<String>(accessToken, mobile);
		String sendMsgPkg = packMessage(faceReq);
		String responseJson = HttpClientFactory.me(false).post(umOapiUrl + "/face/Dd/getSingleUserIdByMobile",
				sendMsgPkg);
		Long dduid = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Long>>() {
		});
		return dduid;
	}

	public Boolean associate(String accessToken, Long dduid) {
		DdFaceRequest<Long> faceReq = new DdFaceRequest<Long>(accessToken, dduid);
		String sendMsgPkg = packMessage(faceReq);
		String responseJson = HttpClientFactory.me(false).post(umOapiUrl + "/face/Dd/associate", sendMsgPkg);
		Boolean ok = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Boolean>>() {
		});
		return ok;
	}

	public Boolean disassociate(String accessToken, Long dduid) {
		DdFaceRequest<Long> faceReq = new DdFaceRequest<Long>(accessToken, dduid);
		String sendMsgPkg = packMessage(faceReq);
		String responseJson = HttpClientFactory.me(false).post(umOapiUrl + "/face/Dd/disassociate", sendMsgPkg);
		Boolean ok = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Boolean>>() {
		});
		return ok;
	}

}
