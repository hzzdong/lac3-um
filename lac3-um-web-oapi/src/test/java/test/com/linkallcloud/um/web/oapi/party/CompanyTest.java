package test.com.linkallcloud.um.web.oapi.party;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.dto.Trees;
import com.linkallcloud.core.face.message.request.IdFaceRequest;
import com.linkallcloud.core.face.message.response.ObjectFaceResponse;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.www.utils.HttpClientFactory;
import com.linkallcloud.um.constant.Consts;
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
	
	@Test
	public void testGetCompanyTree() throws Exception {
		IdFaceRequest req = new IdFaceRequest(10L, "8cd2d9cbbbac49559eb84c4010d47473");
		req.setType(Consts.ORG_TREE_TYPE_FULL);

		String sendMsgPkg = packMessage(req);
		String responseJson = HttpClientFactory.me(false).post(SERVER + "/face/YwCompany/getCompanyTree",
				sendMsgPkg);
		Tree tree = unpackMessage(responseJson, new TypeReference<ObjectFaceResponse<Tree>>() {
		});
		log.info(JSON.toJSONString(tree));
		
		String[] types = {"0"};
		Tree typeTree = Trees.treeFilter(tree, types, true);
		log.info(JSON.toJSONString(typeTree));

		assertNotNull(tree);
	}

}
