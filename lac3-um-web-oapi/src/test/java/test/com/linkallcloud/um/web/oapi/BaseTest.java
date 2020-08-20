package test.com.linkallcloud.um.web.oapi;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.sh.sm.ISignatureMessage;
import com.linkallcloud.sh.sm.SignatureMessage;
import com.linkallcloud.core.face.message.request.FaceRequest;
import com.linkallcloud.core.face.message.request.ObjectFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;

public class BaseTest {
	protected static Log log = Logs.get();

	protected static final String MSG_ENC_ALG = "AES";
	protected static final String MSG_ENC_KEY = "6$TuO!Xr4lJtH@hA";
	protected static final String APP_CODE = "hqzc-zf";
	protected static final String SIGN_KEY = "S9sdfjsk#dGN8j*dfhw";
	protected static final String SIGN_ALG = "SHA1";

	protected static final String SERVER = "http://localhost:8012/umoapi";
//	protected static final String SERVER = "http://47.96.234.18/umoapi";

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
			ISignatureMessage sendSM = new SignatureMessage(request, APP_CODE, SIGN_ALG);
			sendSM.sign(SIGN_KEY);// 签名
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
			receivedSM.verifyStrict(APP_CODE, SIGN_KEY, SIGN_ALG);
			ObjectFaceResponse<T> res = receivedSM.unpackMessage(tr.getType());
			if (res.getCode().equals("0")) {
				return res.getData();
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

}
