package com.linkallcloud.um.dto.holiday;

import java.util.Calendar;

import com.linkallcloud.core.dto.Sid;
import com.linkallcloud.core.face.message.request.LoginFaceRequest;
import com.linkallcloud.core.lang.Strings;

public class HolidayRequest extends LoginFaceRequest {
	private static final long serialVersionUID = 5154977167298864203L;

	private Long companyId;
	private String companyType;

	private String date;

	public HolidayRequest() {
		super();
	}

	public HolidayRequest(Long companyId, String companyType) {
		this(companyId, companyType, null);
	}

	public HolidayRequest(Long companyId, String companyType, String date) {
		super();
		this.companyId = companyId;
		this.companyType = companyType;
		if (Strings.isBlank(date)) {
			Calendar start = Calendar.getInstance();
			this.date = start.get(Calendar.YEAR) + "";
		} else {
			this.date = date;
		}
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Sid getCompany() {
		return new Sid(this.getCompanyId(), null, this.getCompanyType(), null);
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
