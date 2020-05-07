package com.linkallcloud.um.server.activity.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.lang.Mirror;
import com.linkallcloud.um.activity.party.IOrgActivity;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.domain.party.Rel4OrgLeader;
import com.linkallcloud.um.domain.party.User;
import com.linkallcloud.um.server.dao.party.IOrgDao;
import com.linkallcloud.um.server.dao.party.IUserDao;

public abstract class OrgActivity<T extends Org, OD extends IOrgDao<T>, U extends User, UD extends IUserDao<U>>
		extends PartyActivity<T, OD> implements IOrgActivity<T> {

	protected Mirror<U> userMirror;

	@SuppressWarnings("unchecked")
	public OrgActivity() {
		super();
		try {
			this.userMirror = Mirror.me((Class<U>) Mirror.getTypeParams(getClass())[2]);
		} catch (Throwable e) {
			if (log.isWarnEnabled()) {
				log.warn("!!!Fail to get TypeParams for self!", e);
			}
		}
	}

	protected Class<U> getUserClass() {
		return (null == userMirror) ? null : userMirror.getType();
	}

	protected abstract UD getUserDao();

	@Override
	public boolean addLeader(Trace t, Rel4OrgLeader leader) {
		int rows = dao().saveRel4OrgLeader(t, leader);
		return retBool(rows);
	}

	@Override
	public boolean deleteLeader(Trace t, Rel4OrgLeader leader) {
		int rows = dao().deleteRel4OrgLeader(t, leader);
		return retBool(rows);
	}

//	@Override
//    public boolean addLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds) {
//        if (userUuidIds != null && !userUuidIds.isEmpty()) {
//            List<U> dbLeaders = getUserDao().findOrgLeaders(t, orgId, orgUuid);
//
//            List<Rel4OrgLeader> rels = new ArrayList<Rel4OrgLeader>();
//            for (String uuid : userUuidIds.keySet()) {
//                Long userId = userUuidIds.get(uuid);
//                if (!Domains.exist(userId, dbLeaders)) {
//                    rels.add(new Rel4OrgLeader(orgId, userId));
//                }
//            }
//            if (!rels.isEmpty()) {
//                int rows = dao().saveRel4OrgLeader(t, rels);
//                return retBool(rows);
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public boolean deleteLeaders(Trace t, Long orgId, String orgUuid, Map<String, Long> userUuidIds) {
//        if (userUuidIds != null && !userUuidIds.isEmpty()) {
//            List<Long> userIds = Domains.parseIds(userUuidIds);
//            if (userIds != null && userIds.size() > 0) {
//                int rows = dao().deleteRel4OrgLeader(t, orgId, userIds);
//                return retBool(rows);
//            }
//        }
//        return false;
//    }
}
