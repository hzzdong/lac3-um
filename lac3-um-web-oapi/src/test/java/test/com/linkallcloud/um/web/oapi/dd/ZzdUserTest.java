package test.com.linkallcloud.um.web.oapi.dd;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.um.oapi.dd.dto.DdFaceRequest;
import com.linkallcloud.core.www.utils.HttpClientFactory;

import test.com.linkallcloud.um.web.oapi.BaseTest;

public class ZzdUserTest extends BaseTest {

	private static String accessToken = null;

	@Before
	public void testGetAccessToken() throws Exception {
		DdFaceRequest<String> req = new DdFaceRequest<String>();

		String sendMsgPkg = packMessage(req);
		String responseJson = HttpClientFactory.me(false).post(SERVER + "/face/Dd/getAccessToken", sendMsgPkg);
		accessToken = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<String>>() {
		});

		assertNotNull(accessToken);
	}

	@Test
	public void testGetSingleUserIdByMobile() throws Exception {
		DdFaceRequest<String> req = new DdFaceRequest<String>(accessToken, "15305712181");

		String sendMsgPkg = packMessage(req);
		String responseJson = HttpClientFactory.me(false).post(SERVER + "/face/Dd/getSingleUserIdByMobile", sendMsgPkg);
		Long dduid = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Long>>() {
		});

		assertNotNull(dduid);
	}


}
