package com.linkallcloud.um.server.utils;

import java.util.Arrays;
import java.util.List;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseException;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.core.query.Query;
import com.linkallcloud.core.query.rule.Equal;
import com.linkallcloud.core.query.rule.In;
import com.linkallcloud.um.activity.party.IYwCompanyActivity;
import com.linkallcloud.um.activity.party.IYwUserActivity;
import com.linkallcloud.um.activity.sys.IAreaActivity;
import com.linkallcloud.um.domain.party.YwUser;
import com.linkallcloud.um.domain.sys.Area;

public class UmTools {
	private static final Log log = Logs.get();
	
	public static void addAreaCnds4YwUserAppPermission(Trace t, Query query, IYwCompanyActivity ywCompanyActivity,
			IYwUserActivity ywUserActivity, IAreaActivity areaActivity) throws BaseException {
		Equal ywUserIdRule = (Equal) query.getRule4Field("ywUserId");
		Equal appIdRule = (Equal) query.getRule4Field("appId");
		// if (ywUserIdRule == null || appIdRule == null) {
		// log.error("参数错误: ywUserId,appId参数都不能为空。");
		// throw new BaseRuntimeException("100001", "参数错误: ywUserId,appId参数都不能为空。");
		// }

		if (ywUserIdRule == null && appIdRule == null) {
			return;
		}

		Long ywUserId = (Long) ywUserIdRule.getValue();
		Long appId = (Long) appIdRule.getValue();

		YwUser ywUser = ywUserActivity.fetchById(t, ywUserId);
		if (ywUser == null) {
			log.error("参数错误，用于不存在：ywUserId:" + ywUserId);
			throw new BaseRuntimeException("100001", "参数错误，用于不存在：ywUserId:" + ywUserId);
		}

		if (ywUser.getAccount().equals("superadmin")) {
			return;
		}

		List<Long> areaIds = null;
		if (ywUser.isAdmin()) {// 管理员，用公司区域权限
			Long[] areaArray = ywCompanyActivity.getCompanyAreaRootIds(t, ywUser.getCompanyId());
			if (areaArray != null && areaArray.length > 0) {
				areaIds = Arrays.asList(areaArray);
			}
			return;
		} else {// 普通用户，用户区域权限
			areaIds = ywUserActivity.findUserAppPermedAreas(t, ywUserId, appId);
		}

		if (areaIds == null || areaIds.isEmpty()) {
			log.info("您没有任何区域的权限。ywUserId:" + ywUserId);
			throw new BaseException("100002", "参数错误。ywUserId:" + ywUserId);
		}

		Area area0 = areaActivity.fetchById(t, areaIds.get(0));
		Equal lr = (Equal) query.getRule4Field("level");
		if (lr != null) {
			lr.setValue(area0.getLevel());
		} else {
			lr = new Equal("level", area0.getLevel());
			query.addRule(lr);
		}

		In r = (In) query.getRule4Field("areaIds");
		if (r != null) {
			r.setValue(areaIds);
		} else {
			r = new In("areaIds", areaIds);
			query.addRule(r);
		}
	}
	

	/**
	 * 运维人员查找客户单位的时候过滤其区域权限
	 * 
	 * @param t
	 * @param query
	 * @param ywCompanyDao
	 * @param ywUserDao
	 * @param areaDao
	 * @throws BaseException
	 */
//	public static void addAreaCnds4YwUserAppPermission(Trace t, Query query, IYwCompanyDao ywCompanyDao,
//			IYwUserDao ywUserDao, IAreaDao areaDao) throws BaseException {
//		Equal ywUserIdRule = (Equal) query.getRule4Field("ywUserId");
//		Equal appIdRule = (Equal) query.getRule4Field("appId");
//		// if (ywUserIdRule == null || appIdRule == null) {
//		// log.error("参数错误: ywUserId,appId参数都不能为空。");
//		// throw new BaseRuntimeException("100001", "参数错误: ywUserId,appId参数都不能为空。");
//		// }
//
//		if (ywUserIdRule == null && appIdRule == null) {
//			return;
//		}
//
//		Long ywUserId = (Long) ywUserIdRule.getValue();
//		Long appId = (Long) appIdRule.getValue();
//
//		YwUser ywUser = ywUserDao.fetchById(t, ywUserId);
//		if (ywUser == null) {
//			log.error("参数错误，用于不存在：ywUserId:" + ywUserId);
//			throw new BaseRuntimeException("100001", "参数错误，用于不存在：ywUserId:" + ywUserId);
//		}
//
//		if (ywUser.getAccount().equals("superadmin")) {
//			return;
//		}
//
//		List<Long> areaIds = null;
//		if (ywUser.isAdmin()) {// 管理员，用公司区域权限
////			Long[] areaArray = ywCompanyDao.findPermedCompanyAppAreas(t, ywUser.getCompanyId(), appId);
////			if (areaArray != null && areaArray.length > 0) {
////				areaIds = Arrays.asList(areaArray);
////			}
//			return;
//		} else {// 普通用户，用户区域权限
//			areaIds = ywUserDao.findUserAppPermedAreas(t, ywUserId, appId);
//		}
//
//		if (areaIds == null || areaIds.isEmpty()) {
//			log.info("您没有任何区域的权限。ywUserId:" + ywUserId);
//			throw new BaseException("100002", "参数错误。ywUserId:" + ywUserId);
//		}
//
//		Area area0 = areaDao.fetchById(t, areaIds.get(0));
//		Equal lr = (Equal) query.getRule4Field("level");
//		if (lr != null) {
//			lr.setValue(area0.getLevel());
//		} else {
//			lr = new Equal("level", area0.getLevel());
//			query.addRule(lr);
//		}
//
//		In r = (In) query.getRule4Field("areaIds");
//		if (r != null) {
//			r.setValue(areaIds);
//		} else {
//			r = new In("areaIds", areaIds);
//			query.addRule(r);
//		}
//	}

	// /**
	// * 运维人员查找客户单位的时候过滤其区域权限
	// *
	// * @param t
	// * @param page
	// * @param ywUserDao
	// * @param areaDao
	// * @throws BaseException
	// */
	// public static void addAreaCnds4UserAppPermission(Trace t, Page<?> page,
	// IYwUserDao ywUserDao,
	// IAreaDao areaDao) throws BaseException {
	// Equal ywUserIdRule = (Equal) page.getRule4Field("ywUserId");
	// Equal appIdRule = (Equal) page.getRule4Field("appId");
	// // if (ywUserIdRule == null || appIdRule == null) {
	// // log.error("参数错误: ywUserId,appId参数都不能为空。");
	// // throw new BaseRuntimeException("100001", "参数错误: ywUserId,appId参数都不能为空。");
	// // }
	//
	// if (ywUserIdRule == null && appIdRule == null) {
	// return;
	// }
	//
	// Long ywUserId = (Long) ywUserIdRule.getValue();
	// Long appId = (Long) appIdRule.getValue();
	//
	// YwUser ywUser = ywUserDao.fetchById(t, ywUserId);
	// if (ywUser == null) {
	// log.error("参数错误，用于不存在：ywUserId:" + ywUserId);
	// throw new BaseRuntimeException("100001", "参数错误，用于不存在：ywUserId:" + ywUserId);
	// }
	//
	// if (ywUser.getAccount().equals("superadmin")) {
	// return;
	// }
	//
	// List<Long> areaIds = ywUserDao.findUserAppPermedAreas(t, ywUserId, appId);
	// if (areaIds == null || areaIds.isEmpty()) {
	// log.info("您没有任何区域的权限。ywUserId:" + ywUserId);
	// throw new BaseException("100002", "参数错误。ywUserId:" + ywUserId);
	// }
	//
	// Area area0 = areaDao.fetchById(t, areaIds.get(0));
	// Equal lr = (Equal) page.getRule4Field("level");
	// if (lr != null) {
	// lr.setValue(area0.getLevel());
	// } else {
	// lr = new Equal("level", area0.getLevel());
	// page.addRule(lr);
	// }
	//
	// In r = (In) page.getRule4Field("areaIds");
	// if (r != null) {
	// r.setValue(areaIds);
	// } else {
	// r = new In("areaIds", areaIds);
	// page.addRule(r);
	// }
	// }

}
