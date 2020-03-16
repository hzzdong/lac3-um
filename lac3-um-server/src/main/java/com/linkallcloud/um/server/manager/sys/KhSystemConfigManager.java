package com.linkallcloud.um.server.manager.sys;

import com.linkallcloud.core.busilog.annotation.Module;
import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.core.manager.BaseManager;
import com.linkallcloud.um.domain.party.KhCompany;
import com.linkallcloud.um.domain.sys.KhSystemConfig;
import com.linkallcloud.um.iapi.sys.IKhSystemConfigManager;
import com.linkallcloud.um.service.party.IKhCompanyService;
import com.linkallcloud.um.service.sys.IKhSystemConfigService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = IKhSystemConfigManager.class, version = "${dubbo.service.version}")
@Module(name = "系统配置")
public class KhSystemConfigManager extends BaseManager<KhSystemConfig, IKhSystemConfigService>
        implements IKhSystemConfigManager {

    @Autowired
    private IKhSystemConfigService khSystemConfigService;

    @Autowired
    private IKhCompanyService khCompanyService;

    @Override
    public KhSystemConfig fetchByCompanyId(Trace t, Long companyId) {
        KhCompany company = khCompanyService.fetchById(t, companyId);
        if (company.isTopParent()) {
            return service().fetchByCompanyId(t, companyId);
        } else {
            return service().fetchByCompanyId(t, company.rootCompanyId());
        }
    }

    @Override
    public KhSystemConfig save(Trace t, KhSystemConfig entity) {
        KhSystemConfig dbEntity = service().fetchByCompanyId(t, entity.getCompanyId());
        if (dbEntity != null) {
            dbEntity.setRootAreaId(entity.getRootAreaId());
            dbEntity.setEnableOrgPerm(entity.getEnableOrgPerm());
            dbEntity.setEnableAreaPerm(entity.getEnableAreaPerm());
            dbEntity.setLogo(entity.getLogo());
            service().update(t, dbEntity);
            return dbEntity;
        } else {
            Long id =  service().insert(t, entity);
            entity.setId(id);
            return entity;
        }
    }

    @Override
    protected IKhSystemConfigService service() {
        return khSystemConfigService;
    }

}
