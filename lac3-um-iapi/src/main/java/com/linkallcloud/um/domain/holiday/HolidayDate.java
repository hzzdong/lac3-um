package com.linkallcloud.um.domain.holiday;

import java.util.Date;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;

/**
 * holiday date
 * 
 * @author haosh
 */
@ShowName(value = "假日", logFields = "id,date")
public class HolidayDate extends Domain {
	private static final long serialVersionUID = 7774603888726892585L;

	/**
	 * 公司ID
	 */
	private Long companyId;

	/**
	 * 主键 日期yyyy-MM-dd isNullAble:0
	 */
	private String date;
	/**
	 *
	 * isNullAble:0
	 */
	private Integer year;

	/**
	 *
	 * isNullAble:0
	 */
	private Integer month;

	/**
	 *
	 * isNullAble:0
	 */
	private Integer day;

	/**
	 * 0普通工作日1周末2需要补班的工作日3法定节假日 isNullAble:1,defaultVal:0
	 */
	// private Integer status;

	public Long getCompanyId() {
		return companyId;
	}

	public HolidayDate() {
		this.uuid = generateUuid();
		this.createTime = new Date();
		this.status = null;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

}
