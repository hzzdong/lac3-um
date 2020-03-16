package com.linkallcloud.um.web.oapi.face.dd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.web.face.annotation.Face;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.oapi.dd.IZzdUserClient;
import com.linkallcloud.um.oapi.dd.dto.DdFaceRequest;

@Controller
@RequestMapping(value = "/face/Dd", method = RequestMethod.POST)
@Module(name = "浙政钉用户操作")
public class ZzdUserClientFace {

	@Autowired
	private IZzdUserClient zzdUserClient;

	@Face(login = false)
	@RequestMapping(value = "/getAccessToken", method = RequestMethod.POST)
	public @ResponseBody Object getAccessToken(DdFaceRequest<String> faceReq, Trace t) throws Exception {
		return zzdUserClient.getAccessToken();
	}

	@Face(login = false)
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
	public @ResponseBody Object getUserInfo(DdFaceRequest<String> faceReq, Trace t) throws Exception {
		return zzdUserClient.getUserInfo(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getOrgInfo", method = RequestMethod.POST)
	public @ResponseBody Object getOrgInfo(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.getOrgInfo(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getSubOrgInfo", method = RequestMethod.POST)
	public @ResponseBody Object getSubOrgInfo(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.getSubOrgInfo(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getAllSubOrgInfo", method = RequestMethod.POST)
	public @ResponseBody Object getAllSubOrgInfo(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.getAllSubOrgInfo(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getAllMemberInfo", method = RequestMethod.POST)
	public @ResponseBody Object getAllMemberInfo(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.getAllMemberInfo(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getAllOrgInfo", method = RequestMethod.POST)
	public @ResponseBody Object getAllOrgInfo(DdFaceRequest<Object> faceReq, Trace t) throws Exception {
		return zzdUserClient.getAllOrgInfo(faceReq.getAccessToken());
	}

	@Face(login = false)
	@RequestMapping(value = "/getAllRootOrgInfo", method = RequestMethod.POST)
	public @ResponseBody Object getAllRootOrgInfo(DdFaceRequest<Object> faceReq, Trace t) throws Exception {
		return zzdUserClient.getAllRootOrgInfo(faceReq.getAccessToken());
	}

	@Face(login = false)
	@RequestMapping(value = "/getSingleUserIdByMobile", method = RequestMethod.POST)
	public @ResponseBody Object getSingleUserIdByMobile(DdFaceRequest<String> faceReq, Trace t) throws Exception {
		return zzdUserClient.getSingleUserIdByMobile(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getBatchUserIdByMobile", method = RequestMethod.POST)
	public @ResponseBody Object getBatchUserIdByMobile(DdFaceRequest<String> faceReq, Trace t) throws Exception {
		return zzdUserClient.getBatchUserIdByMobile(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getSingleUserIdByDingId", method = RequestMethod.POST)
	public @ResponseBody Object getSingleUserIdByDingId(DdFaceRequest<String> faceReq, Trace t) throws Exception {
		return zzdUserClient.getSingleUserIdByDingId(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getBatchUserIdByDingId", method = RequestMethod.POST)
	public @ResponseBody Object getBatchUserIdByDingId(DdFaceRequest<String> faceReq, Trace t) throws Exception {
		return zzdUserClient.getBatchUserIdByDingId(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/associate", method = RequestMethod.POST)
	public @ResponseBody Object associate(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.associate(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/disassociate", method = RequestMethod.POST)
	public @ResponseBody Object disassociate(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.disassociate(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getSingleDingUserIdByUserId", method = RequestMethod.POST)
	public @ResponseBody Object getSingleDingUserIdByUserId(DdFaceRequest<String> faceReq, Trace t) throws Exception {
		return zzdUserClient.getSingleDingUserIdByUserId(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getBatchDingUserIdByUserId", method = RequestMethod.POST)
	public @ResponseBody Object getBatchDingUserIdByUserId(DdFaceRequest<String> faceReq, Trace t) throws Exception {
		return zzdUserClient.getBatchDingUserIdByUserId(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getDingOrgIdByOrgId", method = RequestMethod.POST)
	public @ResponseBody Object getDingOrgIdByOrgId(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.getDingOrgIdByOrgId(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getAdmin", method = RequestMethod.POST)
	public @ResponseBody Object getAdmin(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.getAdmin(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getAdminScope", method = RequestMethod.POST)
	public @ResponseBody Object getAdminScope(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.getAdminScope(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getOrgPath", method = RequestMethod.POST)
	public @ResponseBody Object getOrgPath(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.getOrgPath(faceReq.getAccessToken(), faceReq.getData());
	}

	@Face(login = false)
	@RequestMapping(value = "/getExtend", method = RequestMethod.POST)
	public @ResponseBody Object getExtend(DdFaceRequest<Long> faceReq, Trace t) throws Exception {
		return zzdUserClient.getExtend(faceReq.getAccessToken(), faceReq.getData());
	}

}
