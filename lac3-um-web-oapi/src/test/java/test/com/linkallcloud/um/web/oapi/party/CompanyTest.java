package test.com.linkallcloud.um.web.oapi.party;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.um.face.TypedOrgTreeRequest;

import test.com.linkallcloud.um.web.oapi.BaseTest;

public class CompanyTest extends BaseTest {
	private static final Log log = Logs.get();

	@Test
	public void testGetTypedCompanyTree() throws Exception {
		int[] types = { 51 };
		TypedOrgTreeRequest req = new TypedOrgTreeRequest(10L, types);

		String sendMsgPkg = packMessage(req);
		String responseJson = HttpClientFactory.me(false).post(SERVER + "/face/YwCompany/getTypedCompanyTree",
				sendMsgPkg);
		Tree tree = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Tree>>() {
		});
		log.info(JSON.toJSONString(tree));

		assertNotNull(tree);
	}

}
