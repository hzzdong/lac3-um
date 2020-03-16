package com.linkallcloud.um.server.activity.party;

import com.linkallcloud.core.activity.BaseTreeActivity;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.um.activity.party.IPartyActivity;
import com.linkallcloud.um.domain.party.Party;
import com.linkallcloud.um.server.dao.party.IPartyDao;

public abstract class PartyActivity<T extends Party, D extends IPartyDao<T>> extends BaseTreeActivity<T, D> implements IPartyActivity<T> {

    public PartyActivity() {
        super();
    }

    @Override
    protected void treeBefore(Trace t, boolean isNew, T entity) throws BaseRuntimeException {
        super.treeBefore(t, isNew, entity);
        if (isNew) {
            entity.setUuid(entity.generateUuid());
            if (entity.isTopParent()) {
                entity.setParentId(0L);
                entity.setParentClass(null);
            }
        } else {
            if (entity.isTopParent()) {
                entity.setParentClass(null);
            }
        }
    }

    @Override
    public T fetchByGovCode(Trace t, String govCode) {
        return dao().fetchByGovCode(t, govCode);
    }
}
