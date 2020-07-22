package com.linkallcloud.um.face;

import com.linkallcloud.core.face.message.request.LoginFaceRequest;

public class CalendarRequest extends LoginFaceRequest {
	private static final long serialVersionUID = 8202665185512304481L;

	private Integer year;
	private Integer month;
	private Integer day;

	public CalendarRequest() {
		super();
	}

	public CalendarRequest(Integer year, Integer month, Integer day) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
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
