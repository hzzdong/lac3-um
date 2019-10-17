package com.linkallcloud.um.pc.controller.party;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linkallcloud.busilog.annotation.WebLog;
import com.linkallcloud.comm.web.controller.BaseLController;
import com.linkallcloud.dto.AppVisitor;
import com.linkallcloud.dto.Result;
import com.linkallcloud.dto.Trace;
import com.linkallcloud.um.domain.party.Org;
import com.linkallcloud.um.iapi.party.IOrgManager;

public abstract class OrgListController<T extends Org, S extends IOrgManager<T>> extends BaseLController<T, S> {

	@Override
	protected String getMainPage() {
		return "page/party/" + getDomainClass().getSimpleName() + "/" + getDomainClass().getSimpleName() + "List";
	}

	@Override
	protected String getEditPage() {
		return "page/party/" + getDomainClass().getSimpleName() + "/" + getDomainClass().getSimpleName() + "Edit";
	}

	@Override
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@WebLog(db = true)
	public @ResponseBody Result<Object> save(@RequestBody @Valid T entity, Trace t, AppVisitor av) {
		if (entity.getId() != null && entity.getId() > 0 && entity.getUuid() != null) {
			manager().update(t, entity);
		} else {
			Long id = manager().insert(t, entity);
			entity.setId(id);
		}
		entity = manager().fetchById(t, entity.getId());
		return new Result<Object>(entity);
	}

}
