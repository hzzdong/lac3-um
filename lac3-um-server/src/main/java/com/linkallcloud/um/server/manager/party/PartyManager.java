package com.linkallcloud.um.server.manager.party;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseTreeManager;
import com.linkallcloud.um.domain.party.Party;
import com.linkallcloud.um.iapi.party.IPartyManager;
import com.linkallcloud.um.service.party.IPartyService;

public abstract class PartyManager<T extends Party, S extends IPartyService<T>> extends BaseTreeManager<T, S>
        implements IPartyManager<T> {

    @Override
    public T fetchByGovCode(Trace t, String govCode) {
        return service().fetchByGovCode(t, govCode);
    }

}
