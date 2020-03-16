package com.linkallcloud.um.server.manager.sys;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.party.YwCompany;
import com.linkallcloud.um.domain.sys.YwSystemConfig;
import com.linkallcloud.um.iapi.sys.IYwSystemConfigManager;
import com.linkallcloud.um.service.party.IYwCompanyService;
import com.linkallcloud.um.service.sys.IYwSystemConfigService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = IYwSystemConfigManager.class, version = "${dubbo.service.version}")
@Module(name = "系统配置")
public class YwSystemConfigManager extends BaseManager<YwSystemConfig, IYwSystemConfigService>
        implements IYwSystemConfigManager {

    @Autowired
    private IYwSystemConfigService ywSystemConfigService;

    @Autowired
    private IYwCompanyService ywCompanyService;

    @Override
    public YwSystemConfig fetchByCompanyId(Trace t, Long companyId) {
        YwCompany company = ywCompanyService.fetchById(t, companyId);
        if (company.isTopParent()) {
            return service().fetchByCompanyId(t, companyId);
        } else {
            return service().fetchByCompanyId(t, company.rootCompanyId());
        }
    }

    @Override
    public YwSystemConfig save(Trace t, YwSystemConfig entity) {
        YwSystemConfig dbEntity = service().fetchByCompanyId(t, entity.getCompanyId());
        if (dbEntity != null) {
            dbEntity.setRootAreaId(entity.getRootAreaId());
            dbEntity.setEnableOrgPerm(entity.getEnableOrgPerm());
            dbEntity.setEnableAreaPerm(entity.getEnableAreaPerm());
            dbEntity.setEnableZf(entity.getEnableZf());
            dbEntity.setEnableZzd(entity.getEnableZzd());
            service().update(t, dbEntity);
            return dbEntity;
        } else {
            Long id = service().insert(t, entity);
            entity.setId(id);
            return entity;
        }
    }

    @Override
    protected IYwSystemConfigService service() {
        return ywSystemConfigService;
    }

}
