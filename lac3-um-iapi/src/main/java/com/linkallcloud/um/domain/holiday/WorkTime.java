package com.linkallcloud.um.domain.holiday;

import com.linkallcloud.core.domain.Domain;

public class WorkTime extends Domain {
	private static final long serialVersionUID = 6306817295964289868L;

	/**
	 * 公司ID
	 */
	private Long companyId;

	private Integer amGoToWorkHour;// 上午上班时间：X点
	private Integer amGoToWorkMinute;// 上午上班时间：X分

	private Integer amGoOffWorkHour;// 上午下班：X点
	private Integer amGoOffWorkMinute;// 上午下班：X分

	private Integer pmGoToWorkHour;// 下午上班时间：X点
	private Integer pmGoToWorkMinute;// 下午上班时间：X分

	private Integer pmGoOffWorkHour;// 下午下班：X点
	private Integer pmGoOffWorkMinute;// 下午下班：X分

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Integer getAmGoToWorkHour() {
		return amGoToWorkHour;
	}

	public void setAmGoToWorkHour(Integer amGoToWorkHour) {
		this.amGoToWorkHour = amGoToWorkHour;
	}

	public Integer getAmGoToWorkMinute() {
		return amGoToWorkMinute;
	}

	public void setAmGoToWorkMinute(Integer amGoToWorkMinute) {
		this.amGoToWorkMinute = amGoToWorkMinute;
	}

	public Integer getAmGoOffWorkHour() {
		return amGoOffWorkHour;
	}

	public void setAmGoOffWorkHour(Integer amGoOffWorkHour) {
		this.amGoOffWorkHour = amGoOffWorkHour;
	}

	public Integer getAmGoOffWorkMinute() {
		return amGoOffWorkMinute;
	}

	public void setAmGoOffWorkMinute(Integer amGoOffWorkMinute) {
		this.amGoOffWorkMinute = amGoOffWorkMinute;
	}

	public Integer getPmGoToWorkHour() {
		return pmGoToWorkHour;
	}

	public void setPmGoToWorkHour(Integer pmGoToWorkHour) {
		this.pmGoToWorkHour = pmGoToWorkHour;
	}

	public Integer getPmGoToWorkMinute() {
		return pmGoToWorkMinute;
	}

	public void setPmGoToWorkMinute(Integer pmGoToWorkMinute) {
		this.pmGoToWorkMinute = pmGoToWorkMinute;
	}

	public Integer getPmGoOffWorkHour() {
		return pmGoOffWorkHour;
	}

	public void setPmGoOffWorkHour(Integer pmGoOffWorkHour) {
		this.pmGoOffWorkHour = pmGoOffWorkHour;
	}

	public Integer getPmGoOffWorkMinute() {
		return pmGoOffWorkMinute;
	}

	public void setPmGoOffWorkMinute(Integer pmGoOffWorkMinute) {
		this.pmGoOffWorkMinute = pmGoOffWorkMinute;
	}

}
