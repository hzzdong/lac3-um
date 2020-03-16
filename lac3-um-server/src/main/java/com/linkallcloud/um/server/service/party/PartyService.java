package com.linkallcloud.um.server.service.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.service.BaseTreeService;
import com.linkallcloud.um.activity.party.IPartyActivity;
import com.linkallcloud.um.domain.party.Party;
import com.linkallcloud.um.service.party.IPartyService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class PartyService<T extends Party, M extends IPartyActivity<T>> extends BaseTreeService<T, M>
        implements IPartyService<T> {

    public PartyService() {
        super();
    }

    @Override
    public T fetchByGovCode(Trace t, String govCode) {
        return activity().fetchByGovCode(t, govCode);
    }

}
