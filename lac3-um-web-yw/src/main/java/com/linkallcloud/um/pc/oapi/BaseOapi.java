package com.linkallcloud.um.pc.oapi;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.security.MsgSignObject;
import com.linkallcloud.sh.sm.ISignatureMessage;
import com.linkallcloud.sh.sm.SignatureMessage;
import com.linkallcloud.um.domain.sys.Application;
import com.linkallcloud.um.iapi.sys.IApplicationManager;

public abstract class BaseOapi {
	protected static Log log = Logs.get();

	@DubboReference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IApplicationManager applicationManager;

	@Value("${oapi.url.um}")
	protected String umOapiUrl;

	@Value("${oapi.appcode}")
	protected String myAppCode;// 签名者ID(appKey)//用code替代

	protected MsgSignObject mso;

	public MsgSignObject getMso() {
		if (mso == null) {
			loadMso();
		}
		return mso;
	}

	private synchronized void loadMso() {
		if (mso == null) {
			Application app = applicationManager.fetchByCode(new Trace(true), myAppCode);
			if (app != null) {
				mso = new MsgSignObject(app.getCode(), app.getSignatureAlg(), app.getSignatureKey(),
						app.getMessageEncAlg(), app.getMessageEncKey());
				mso.setTimeout(app.getTimeout() < 10 ? 10 : app.getTimeout());
			} else {
				log.errorf("加载Application失败：%s", myAppCode);
			}
		}
	}

	public FaceRequest convertToFaceRequest(Object message) {
		FaceRequest request = null;
		if (message == null) {
			request = new ObjectFaceRequest<Object>();
		} else if (message.getClass().getName().equals("java.lang.String")) {
			String temp = (String) message;
			if (Strings.isBlank(temp)) {
				request = new ObjectFaceRequest<String>();
			} else {
				request = new ObjectFaceRequest<String>(temp);
			}
		} else if (!(message instanceof FaceRequest)) {
			request = new ObjectFaceRequest<Object>(message);
		} else {
			request = (FaceRequest) message;
		}
		return request;
	}

	public String packMessage(Object message) {
		try {
			FaceRequest request = convertToFaceRequest(message);
			ISignatureMessage sendSM = new SignatureMessage(request, myAppCode, getMso().getSignatureAlg());
			sendSM.sign(getMso().getSignatureKey());// 签名
			String sendMsgPkg = sendSM.packMessage();// 打包后的安全消息
			log.debug("*********** 打包后准备发送数据包：" + sendMsgPkg);
			return sendMsgPkg;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public <T> T unpackMessage(String responseJson, TypeReference<ObjectFaceResponse<T>> tr) {
		log.debug("*********** 接收到数据包：" + responseJson);
		try {
			ISignatureMessage receivedSM = SignatureMessage.from(responseJson);
			receivedSM.verifyStrict(myAppCode, getMso().getSignatureKey(), getMso().getSignatureAlg());
			ObjectFaceResponse<T> res = receivedSM.unpackMessage(tr.getType());
			if (res.getCode().equals("0")) {
				return res.getData();
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public String packMessage4App(Object message, Application app) {
		try {
			FaceRequest request = convertToFaceRequest(message);
			ISignatureMessage sendSM = new SignatureMessage(request, app.getCode(), app.getSignatureAlg());
			sendSM.sign(app.getSignatureKey());// 签名
			String sendMsgPkg = sendSM.packMessage();// 打包后的安全消息
			log.debug("*********** 打包后准备发送数据包：" + sendMsgPkg);
			return sendMsgPkg;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public <T> T unpackMessage4App(String responseJson, TypeReference<ObjectFaceResponse<T>> tr, Application app) {
		log.debug("*********** 接收到数据包：" + responseJson);
		try {
			ISignatureMessage receivedSM = SignatureMessage.from(responseJson);
			receivedSM.verifyStrict(app.getCode(), app.getSignatureKey(), app.getSignatureAlg());
			ObjectFaceResponse<T> res = receivedSM.unpackMessage(tr.getType());
			if (res.getCode().equals("0")) {
				return res.getData();
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public String getUmOapiUrl() {
		return umOapiUrl;
	}

	public String getMyAppCode() {
		return myAppCode;
	}

}
