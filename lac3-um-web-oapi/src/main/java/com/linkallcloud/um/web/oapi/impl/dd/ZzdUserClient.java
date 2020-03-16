package com.linkallcloud.um.web.oapi.impl.dd;

import java.net.ConnectException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.exception.Exceptions;
import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.um.oapi.dd.IZzdUserClient;
import com.linkallcloud.um.oapi.dd.dto.ZZDOrgBean;
import com.linkallcloud.um.oapi.dd.dto.ZZDOrgUserBean;
import com.linkallcloud.um.oapi.dd.dto.ZZDSubOrgBean;
import com.linkallcloud.um.oapi.dd.dto.ZZDSubUserBean;
import com.linkallcloud.um.oapi.dd.dto.ZZDUserBean;
import com.linkallcloud.core.www.utils.HttpClientFactory;

/**
 * 浙政钉用户对接相关工具类 1.获取accessToken 2.获取jsapi中的ticket 3.根据传入的临时code获取用户的基本信息
 * 4.根据userid获取详细用户信息
 *
 */
@Component
public class ZzdUserClient implements IZzdUserClient {
	private static Log log = Logs.get();

	@Value("${zzduser.appid}")
	private String appid;
	@Value("${zzduser.appsecret}")
	private String appSecret;
	@Value("${zzduser.api.url}")
	private String apiUrl;
	@Value("${zzduser.virtual.org}")
	private String zzdVirtualOrg;

	// √3.1 获取AccessToken
	private String GET_TOKEN_URL = "%s/oapi/getToken?appid=%s&appsecret=%s";
	// √3.2 获取人员信息
	private String USER_INFO_URL = "%s/oapi/user/get?access_token=%s&userId=%s";
	// 3.6 获取节点信息
	private String ORG_INFO_URL = "%s/oapi/org/get?access_token=%s&orgNumber=%s";
	// 3.10 获取子节点列表(当前节点下的单层子节点)
	private String SUBORG_INFO_URL = "%s/oapi/org/suborg/get?access_token=%s&orgNumber=%s";
	// 3.11 获取子节点列表(当前节点下所有节点)
	private String ALLSUBORG_INFO_URL = "%s/oapi/org/getAllSubOrg?access_token=%s&orgNumber=%s";
	// 3.12 获取节点成员列表
	private String ALLMEMBER_INFO_URL = "%s/oapi/org/member/get?access_token=%s&orgNumber=%s";
	// 3.13 获取所有节点列表
	private String ALLORG_INFO_URL = "%s/oapi/org/getall?access_token=%s";
	// 3.14 获取所有根节点列表
	private String ALLROOTORG_INFO_URL = "%s/oapi/org/getRootOrg?access_token=%s";
	// √3.16 根据手机号码获取用户ID
	private String GETSINGLEUSERIdByMobile_URL = "%s/oapi/user/singleGetUserIdByMobile?access_token=%s&mobile=%s";
	// √3.17 根据手机号批量获取用户ID
	private String GETBATCHUSERIdByMobile_URL = "%s/oapi/user/batchGetUserIdByMobile?access_token=%s&mobile=%s";
	// 3.18 根据DingUserId获取用户ID
	private String GETSINGLEUSERIdByDINGID_URL = "%s/oapi/user/singleGetUserIdByDingId?access_token=%s&dingUserId=%s";
	// 3.19 根据DingUserId批量获取用户ID
	private String GETBATCHUSERIdByDINGID_URL = "%s/oapi/user/batchGetUserIdByDingId?access_token=%s";
	// √3.20 人员跨地域兼职
	private String ASSOCIATE_URL = "%s/oapi/user/associate?access_token=%s";
	// √3.21 人员跨地域离职
	private String DISASSOCIATE_URL = "%s/oapi/user/disassociate?access_token=%s";
	// 3.25 根据userId获取DingUserId
	private String GETSINGLEDINGUSERIDBYUSERID_URL = "%s/oapi/user/singleGetDingUserIdByUserId?access_token=%s&userId=%s";
	// 3.26 根据userId批量获取DingUserId
	private String GETBATCHDINGUSERIDBYUSERID_URL = "%s/oapi/user/batchGetDingUserIdByUserId?access_token=%s&userId=%s";
	// 3.27 根据orgId获取DingOrgId
	private String GETDINGORGIDBYORGID_URL = "%s/oapi/org/getDingOrgIdByOrgId?access_token=%s&orgNumber=%s";
	// 3.28 获取某个节点下的管理员列表
	private String GETADMIN_URL = "%s/oapi/user/getAdmin?access_token=%s&orgNumber=%s";
	// 3.29 获取管理员范围
	private String GETADMINSCOPE_URL = "%s/oapi/user/getAdminScope?access_token=%s&userId=%s";
	// 3.30 获取部门链
	private String GETORGPATH_URL = "%s/oapi/org/getOrgPath?access_token=%s&orgNumber=%s";
	// 3.31 获取人员附属信息
	private String GETEXTEND_URL = "%s/v1/oapi/user/getExtend?access_token=%s&userId=%s";

	// 调整到1小时50分钟
	public static final long cacheTime = 1000 * 60 * 55 * 2;

	private static String ZZDACCESS_TOKEN = null;
	private static long ZZD_LAST_TIME = 0;

	/**
	 * 获取accessToken
	 * 
	 * @param corpId
	 * @param corpSecert
	 * @return 与数梦服务器请求生成的accessToken
	 * @throws ApiException
	 */
	@Override
	public String getAccessToken() throws BaseRuntimeException {
		long curTime = System.currentTimeMillis();
		long differ = curTime - ZZD_LAST_TIME;

		if (ZZDACCESS_TOKEN != null && differ < cacheTime)
			return ZZDACCESS_TOKEN;

		ZZDACCESS_TOKEN = requestAccessToken();
		ZZD_LAST_TIME = curTime;

		return ZZDACCESS_TOKEN;
	}

	/**
	 * 获取accessToken
	 * 
	 * @return
	 * @throws BaseRuntimeException
	 */
	private String requestAccessToken() throws BaseRuntimeException {
		String accessToken = null;
		String requestUrl = String.format(GET_TOKEN_URL, apiUrl, appid, appSecret);
		try {
			String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
			log.infof("获取到的json格式的Token为:%s ", json);
			// 判断json是否为空
			if (json != null) {
				JSONObject jsonObject = JSONObject.parseObject(json);
				String retCode = jsonObject.getString("retCode");
				if ("0".equals(retCode)) {
					JSONObject retData = (JSONObject) jsonObject.get("retData");
					accessToken = retData.getString("token");
					log.infof("获取到的access_token为:%s ", accessToken);
				} else {
					String retMessage = jsonObject.getString("retMessage");
					log.errorf("获取token失败 errcode:%s errmsg:%s ", retCode, retMessage);
				}
			} else {
				// 获取token失败
				log.error("获取token失败,获取到的json为空");
			}
		} catch (Throwable e) {
			log.error("获取token失败:" + e.getMessage(), e);
			throw new BaseRuntimeException(Exceptions.CODE_ERROR_UNKNOW, "获取浙政钉access_token失败");
		}
		return accessToken;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param userId      员工在企业内的UserID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public ZZDUserBean getUserInfo(String accessToken, String userId) {
		String requestUrl = String.format(USER_INFO_URL, apiUrl, accessToken, userId);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		ZZDUserBean userBean = null;
		log.infof("获取到的用户信息json格式为:%s ", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				JSONObject retData = (JSONObject) jsonObject.get("retData");
				log.infof("获取到的用户信息为:%s ", retData.toJSONString());
				userBean = JSON.parseObject(retData.toJSONString(), new TypeReference<ZZDUserBean>() {
				});
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取token失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			// 获取用户信息失败
			log.error("获取token失败,获取到的json为空");
		}
		return userBean;
	}

	/**
	 * 获取节点信息
	 * 
	 * @param orgNumber   节点唯一标识ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public ZZDOrgBean getOrgInfo(String accessToken, Long orgNumber) {
		String requestUrl = String.format(ORG_INFO_URL, apiUrl, accessToken, orgNumber);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		ZZDOrgBean orgBean = null;
		log.infof("获取到的节点信息json格式为:%s ", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				JSONObject retData = (JSONObject) jsonObject.get("retData");
				log.infof("获取到的节点信息为:%s ", retData.toJSONString());
				orgBean = JSON.parseObject(retData.toJSONString(), new TypeReference<ZZDOrgBean>() {
				});
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取节点信息失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("获取节点信息失败,获取到的json为空");
		}
		return orgBean;
	}

	/**
	 * 获取单层子节点信息
	 * 
	 * @param orgNumber   节点唯一标识ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public List<ZZDSubOrgBean> getSubOrgInfo(String accessToken, Long orgNumber) {
		String requestUrl = String.format(SUBORG_INFO_URL, apiUrl, accessToken, orgNumber);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		List<ZZDSubOrgBean> subOrgBean = null;
		log.infof("获取到的单层子节点信息json格式为:%s ", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				JSONObject retData = (JSONObject) jsonObject.get("retData");
				log.infof("获取到的单层子节点信息为:%s ", retData.toJSONString());
				subOrgBean = JSON.parseObject(retData.toJSONString(), new TypeReference<List<ZZDSubOrgBean>>() {
				});
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取单层子节点信息失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("获取单层子节点信息失败,获取到的json为空");
		}
		return subOrgBean;
	}

	/**
	 * 获取所有子节点信息
	 * 
	 * @param orgNumber   节点唯一标识ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public List<ZZDSubOrgBean> getAllSubOrgInfo(String accessToken, Long orgNumber) {
		String requestUrl = String.format(ALLSUBORG_INFO_URL, apiUrl, accessToken, orgNumber);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		List<ZZDSubOrgBean> subOrgBeanList = null;
		log.infof("获取到的所有子节点信息json格式为:%s ", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				JSONObject retData = (JSONObject) jsonObject.get("retData");
				log.infof("获取到的所有子节点信息为:%s ", retData.toJSONString());
				subOrgBeanList = JSON.parseObject(retData.toJSONString(), new TypeReference<List<ZZDSubOrgBean>>() {
				});
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取所有子节点信息失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("获取所有子节点信息失败,获取到的json为空");
		}
		return subOrgBeanList;
	}

	/**
	 * 获取节点成员列表
	 * 
	 * @param orgNumber   节点唯一标识ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public List<ZZDSubUserBean> getAllMemberInfo(String accessToken, Long orgNumber) {
		String requestUrl = String.format(ALLMEMBER_INFO_URL, apiUrl, accessToken, orgNumber);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		List<ZZDSubUserBean> subUserBeanList = null;
		log.infof("获取到的节点成员列表json格式为:%s ", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				JSONObject retData = (JSONObject) jsonObject.get("retData");
				log.infof("获取到的节点成员列表为:%s ", retData.toJSONString());
				subUserBeanList = JSON.parseObject(retData.toJSONString(), new TypeReference<List<ZZDSubUserBean>>() {
				});
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取节点成员列表失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("获取节点成员列表失败,获取到的json为空");
		}
		return subUserBeanList;
	}

	/**
	 * 获取所有节点列表
	 * 
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public List<ZZDSubOrgBean> getAllOrgInfo(String accessToken) {
		String requestUrl = String.format(ALLORG_INFO_URL, apiUrl, accessToken);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		List<ZZDSubOrgBean> subUserBeanList = null;
		log.infof("获取到的所有节点列表json格式为:%s ", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				JSONObject retData = (JSONObject) jsonObject.get("retData");
				log.infof("获取到的所有节点列表为:%s ", retData.toJSONString());
				subUserBeanList = JSON.parseObject(retData.toJSONString(), new TypeReference<List<ZZDSubOrgBean>>() {
				});
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取所有节点列表失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("获取所有节点列表失败,获取到的json为空");
		}
		return subUserBeanList;
	}

	/**
	 * 获取所有根节点列表
	 * 
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public List<ZZDSubOrgBean> getAllRootOrgInfo(String accessToken) {
		String requestUrl = String.format(ALLROOTORG_INFO_URL, apiUrl, accessToken);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.getRequest(requestUrl);
		List<ZZDSubOrgBean> subUserBeanList = null;
		log.infof("获取到的所有根节点列表json格式为:%s ", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				JSONArray retData = (JSONArray) jsonObject.get("retData");
				log.infof("获取到的所有根节点列表为:%s ", retData.toJSONString());
				// subUserBeanList = JSON.parseObject(retData.toJSONString(), new
				// TypeReference<List<ZZDSubOrgBean>>() {});
				subUserBeanList = retData.toJavaObject(new TypeReference<List<ZZDSubOrgBean>>() {
				});
				subUserBeanList = retData.toJavaList(ZZDSubOrgBean.class);
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取所有根节点列表失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("获取所有根节点列表失败,获取到的json为空");
		}
		return subUserBeanList;
	}

	/**
	 * 根据手机号码获取用户ID
	 * 
	 * @param mobile      手机号
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public Long getSingleUserIdByMobile(String accessToken, String mobile) {
		String requestUrl = String.format(GETSINGLEUSERIdByMobile_URL, apiUrl, accessToken, mobile);
		String json = HttpClientFactory.me(false).post(requestUrl, "");// HttpUtil.postRequest(requestUrl);
		Long userId = null;
		log.infof("根据手机号码获取用户IDjson格式为:%s", json);
		// 判断json是否为空
		if (!Strings.isBlank(json)) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				userId = jsonObject.getLong("retData");
				log.infof("根据手机号码获取用户ID为:%s ", userId);
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("根据手机号码获取用户ID失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("根据手机号码获取用户ID失败,获取到的json为空");
		}
		return userId;
	}

	/**
	 * 根据手机号批量获取用户ID
	 * 
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 * @throws ConnectException
	 */
	// {"retCode":36009,"retMessage":"用户所在组织未授权","retData":{"15088629515":null}}
	@Override
	public JSONObject getBatchUserIdByMobile(String accessToken, String mobiles) throws ConnectException {
		String requestUrl = String.format(GETBATCHUSERIdByMobile_URL, apiUrl, accessToken);
		String json = HttpClientFactory.me(false).post(requestUrl, mobiles);// HttpUtil.httpRaw(requestUrl, mobiles);
		JSONObject relUserId = null;
		log.infof("根据手机号码批量获取用户IDjson格式为:%s ", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				relUserId = (JSONObject) jsonObject.get("retData");
				log.infof("根据手机号码批量获取用户ID为:%s ", relUserId.toJSONString());
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("根据手机号码批量获取用户ID失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("根据手机号码批量获取用户ID失败,获取到的json为空");
		}
		return relUserId;
	}

	/**
	 * 根据dingUserId获取用户ID
	 * 
	 * @param dingUserId  钉钉中，该用户的ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public String getSingleUserIdByDingId(String accessToken, String dingUserId) {
		String requestUrl = String.format(GETSINGLEUSERIdByDINGID_URL, apiUrl, accessToken, dingUserId);
		String json = HttpClientFactory.me(false).post(requestUrl, "");// HttpUtil.postRequest(requestUrl, null);
		String userId = null;
		log.infof("根据dingUserId获取用户IDjson格式为:%s", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				userId = jsonObject.getString("retData");
				log.infof("根据dingUserId获取用户ID为:%s ", userId);
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("根据dingUserId获取用户ID失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("根据dingUserId获取用户ID失败,获取到的json为空");
		}
		return userId;
	}

	/**
	 * 根据dingUserId批量获取用户ID
	 * 
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 * @throws ConnectException
	 */
	@Override
	public JSONObject getBatchUserIdByDingId(String accessToken, String dingUserIds) throws ConnectException {
		String requestUrl = String.format(GETBATCHUSERIdByDINGID_URL, apiUrl, accessToken);
		String json = HttpClientFactory.me(false).post(requestUrl, dingUserIds);// HttpUtil.httpRaw(requestUrl,
																				// dingUserIds);
		JSONObject relUserId = null;
		log.infof("根据dingUserId批量获取用户IDjson格式为:%s ", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				relUserId = (JSONObject) jsonObject.get("retData");
				log.infof("根据dingUserId批量获取用户ID为:%s ", relUserId.toJSONString());
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("根据dingUserId批量获取用户ID失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("根据dingUserId批量获取用户ID失败,获取到的json为空");
		}
		return relUserId;
	}

	/**
	 * 人员跨地域兼职
	 * 
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @param orgNumber   节点编号
	 * @param userId      人员ID
	 * @return 用户相关信息
	 * @throws ConnectException
	 */
	@Override
	public boolean associate(String accessToken, Long userId) {
		boolean result = false;
		try {
			String requestUrl = String.format(ASSOCIATE_URL, apiUrl, accessToken);
			ZZDOrgUserBean orgUser = new ZZDOrgUserBean();
			orgUser.setOrgNumber(Long.parseLong(zzdVirtualOrg));
			orgUser.setUserId(userId);
			String paramJson = JSON.toJSONString(orgUser);
			log.infof("请求人员跨地域兼职的json参数为:%s ", paramJson);
			String json = HttpClientFactory.me(false).post(requestUrl, paramJson);// HttpUtil.httpRaw(requestUrl,
																					// paramJson);
			log.infof("人员跨地域兼职返回的json格式为:%s ", json);
			// 判断json是否为空
			if (json != null) {
				JSONObject jsonObject = JSONObject.parseObject(json);
				String retCode = jsonObject.getString("retCode");
				if ("0".equals(retCode)) {
					result = true;
					log.infof("人员跨地域兼职成功！");
				} else {
					String retMessage = jsonObject.getString("retMessage");
					log.errorf("人员跨地域兼职 失败 errcode:%s errmsg:%s ", retCode, retMessage);
				}
			} else {
				log.error("人员跨地域兼职 失败,获取到的json为空");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 人员跨地域离职
	 * 
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @param orgNumber   节点编号
	 * @param userId      人员ID
	 * @return 用户相关信息
	 * @throws ConnectException
	 */
	@Override
	public boolean disassociate(String accessToken, Long userId) {
		boolean result = false;
		try {
			String requestUrl = String.format(DISASSOCIATE_URL, apiUrl, accessToken);
			ZZDOrgUserBean orgUser = new ZZDOrgUserBean();
			orgUser.setOrgNumber(Long.parseLong(zzdVirtualOrg));
			orgUser.setUserId(userId);
			String paramJson = JSON.toJSONString(orgUser);
			log.infof("请求人员跨地域离职的json参数为:%s ", paramJson);
			String json = HttpClientFactory.me(false).post(requestUrl, paramJson);// HttpUtil.httpRaw(requestUrl,
																					// paramJson);
			log.infof("人员跨地域离职返回的json格式为:%s ", json);
			// 判断json是否为空
			if (json != null) {
				JSONObject jsonObject = JSONObject.parseObject(json);
				String retCode = jsonObject.getString("retCode");
				if ("0".equals(retCode)) {
					result = true;
					log.infof("人员跨地域离职成功！");
				} else {
					String retMessage = jsonObject.getString("retMessage");
					log.errorf("人员跨地域离职失败 errcode:%s errmsg:%s ", retCode, retMessage);
				}
			} else {
				log.error("人员跨地域离职失败,获取到的json为空");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 根据用户Id获取dingUserId
	 * 
	 * @param userId      员工唯一标识ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public String getSingleDingUserIdByUserId(String accessToken, String userId) {
		String requestUrl = String.format(GETSINGLEDINGUSERIDBYUSERID_URL, apiUrl, accessToken, userId);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		String dingUserId = null;
		log.infof("根据用户ID获取dingUserIdjson格式为:%s", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				dingUserId = jsonObject.getString("retData");
				log.infof("根据用户ID获取dingUserId为:%s ", userId);
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("根据用户ID获取dingUserId失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("根据用户ID获取dingUserId失败,获取到的json为空");
		}
		return dingUserId;
	}

	/**
	 * 根据用户ID批量获取dingUserId
	 * 
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 * @throws ConnectException
	 */
	@Override
	public JSONObject getBatchDingUserIdByUserId(String accessToken, String userIds) throws ConnectException {
		String requestUrl = String.format(GETBATCHDINGUSERIDBYUSERID_URL, apiUrl, accessToken);
		String json = HttpClientFactory.me(false).post(requestUrl, userIds);// HttpUtil.httpRaw(requestUrl, userIds);
		JSONObject relDingUserId = null;
		log.infof("根据用户ID批量获取dingUserIdjson格式为:%s ", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				relDingUserId = (JSONObject) jsonObject.get("retData");
				log.infof("根据用户ID批量获取dingUserId为:%s ", relDingUserId.toJSONString());
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("根据用户ID批量获取dingUserId失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("根据用户ID批量获取dingUserId,获取到的json为空");
		}
		return relDingUserId;
	}

	/**
	 * 根据orgId获取dingOrgId
	 * 
	 * @param orgNumber   节点ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public String getDingOrgIdByOrgId(String accessToken, Long orgNumber) {
		String requestUrl = String.format(GETDINGORGIDBYORGID_URL, apiUrl, accessToken, orgNumber);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		String dingOrgId = null;
		log.infof("根据手机号码获取用户IDjson格式为:%s", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				dingOrgId = jsonObject.getString("retData");
				log.infof("根据手机号码获取用户ID为:%s ", dingOrgId);
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("根据手机号码获取用户ID失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("根据手机号码获取用户ID失败,获取到的json为空");
		}
		return dingOrgId;
	}

	/**
	 * 获取某个节点下的管理员列表
	 * 
	 * @param orgNumber   节点ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public List<Long> getAdmin(String accessToken, Long orgNumber) {
		List<Long> adminUserIdList = null;
		String requestUrl = String.format(GETADMIN_URL, apiUrl, accessToken, orgNumber);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		JSONArray adminUserIds = null;
		log.infof("获取某个节点下的管理员列表json格式为:%s", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				adminUserIds = (JSONArray) jsonObject.get("retData");
				String adminUserIdsJson = adminUserIds.toJSONString();
				log.infof("获取某个节点下的管理员列表为:%s ", adminUserIdsJson);
				adminUserIdList = JSON.parseObject(adminUserIdsJson, new TypeReference<List<Long>>() {
				});
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取某个节点下的管理员列表失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("获取某个节点下的管理员列表失败,获取到的json为空");
		}
		return adminUserIdList;
	}

	/**
	 * 根据管理员ID获取管理员的管理范围
	 * 
	 * @param userId      人员ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public List<Long> getAdminScope(String accessToken, Long userId) {
		List<Long> adminOrgIdList = null;
		String requestUrl = String.format(GETADMINSCOPE_URL, apiUrl, accessToken, userId);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		JSONArray adminOrgIds = null;
		log.infof("获取管理员范围列表json格式为:%s", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				adminOrgIds = (JSONArray) jsonObject.get("retData");
				String adminOrgIdsJson = adminOrgIds.toJSONString();
				log.infof("获取管理员范围列表为:%s ", adminOrgIdsJson);
				adminOrgIdList = JSON.parseObject(adminOrgIdsJson, new TypeReference<List<Long>>() {
				});
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取管理员范围列表失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("获取管理员范围列表失败,获取到的json为空");
		}
		return adminOrgIdList;
	}

	/**
	 * 获取某个节点的所有父节点ID列表
	 * 
	 * @param userId      人员ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 用户相关信息
	 */
	@Override
	public List<Long> getOrgPath(String accessToken, Long orgNumber) {
		List<Long> OrgPathList = null;
		String requestUrl = String.format(GETORGPATH_URL, apiUrl, accessToken, orgNumber);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		JSONArray orgIds = null;
		log.infof("获取某个节点的所有父节点ID列表json格式为:%s", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				orgIds = (JSONArray) jsonObject.get("retData");
				String orgIdsJson = orgIds.toJSONString();
				log.infof("获取某个节点的所有父节点ID列表为:%s ", orgIdsJson);
				OrgPathList = JSON.parseObject(orgIdsJson, new TypeReference<List<Long>>() {
				});
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取某个节点的所有父节点ID列表失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("获取某个节点的所有父节点ID列表失败,获取到的json为空");
		}
		return OrgPathList;
	}

	/**
	 * 获取人员的额外属性，包括主节点
	 * 
	 * @param userId      人员ID
	 * @param accessToken 根据相应appid和appsecret生成的access_token
	 * @return 人员额外属性
	 */
	@Override
	public ZZDUserBean getExtend(String accessToken, Long userId) {
		String requestUrl = String.format(GETEXTEND_URL, apiUrl, accessToken, userId);
		String json = HttpClientFactory.me(false).get(requestUrl);// HttpUtil.postRequest(requestUrl);
		ZZDUserBean userExtend = null;
		log.infof("获取人员的额外属性json格式为:%s", json);
		// 判断json是否为空
		if (json != null) {
			JSONObject jsonObject = JSONObject.parseObject(json);
			String retCode = jsonObject.getString("retCode");
			if ("0".equals(retCode)) {
				JSONObject retData = (JSONObject) jsonObject.get("retData");
				log.infof("获取人员的额外属性为:%s ", retData.toJSONString());
				userExtend = JSON.parseObject(retData.toJSONString(), new TypeReference<ZZDUserBean>() {
				});
			} else {
				String retMessage = jsonObject.getString("retMessage");
				log.errorf("获取人员的额外属性失败 errcode:%s errmsg:%s ", retCode, retMessage);
			}
		} else {
			log.error("获取人员的额外属性失败,获取到的json为空");
		}
		return userExtend;
	}
}