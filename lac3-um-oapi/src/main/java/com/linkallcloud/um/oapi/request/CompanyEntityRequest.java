package com.linkallcloud.um.oapi.request;

import com.linkallcloud.core.face.message.request.FaceRequest;

public class CompanyEntityRequest extends FaceRequest {
	private static final long serialVersionUID = -6745209675273872967L;
	
	private Long companyId;
    private Long entityId;

    public CompanyEntityRequest(Long companyId, Long entityId) {
        this.companyId = companyId;
        this.entityId = entityId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
