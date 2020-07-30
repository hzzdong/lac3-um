package com.linkallcloud.um.dto.holiday;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.linkallcloud.core.lang.Strings;
import com.linkallcloud.core.vo.Vo;

public class WorkTimeVo extends Vo {
	private static final long serialVersionUID = -1666917206649388544L;

	private Integer amGoToWorkHour;// 上午上班时间：X点
	private Integer amGoToWorkMinute;// 上午上班时间：X分

	private Integer amGoOffWorkHour;// 上午下班：X点
	private Integer amGoOffWorkMinute;// 上午下班：X分

	private Integer pmGoToWorkHour;// 下午上班时间：X点
	private Integer pmGoToWorkMinute;// 下午上班时间：X分

	private Integer pmGoOffWorkHour;// 下午下班：X点
	private Integer pmGoOffWorkMinute;// 下午下班：X分

	public WorkTimeVo() {
		super();
	}

	public WorkTimeVo(Integer amGoToWorkHour, Integer amGoToWorkMinute, Integer amGoOffWorkHour,
			Integer amGoOffWorkMinute, Integer pmGoToWorkHour, Integer pmGoToWorkMinute, Integer pmGoOffWorkHour,
			Integer pmGoOffWorkMinute) {
		this.amGoToWorkHour = amGoToWorkHour;
		this.amGoToWorkMinute = amGoToWorkMinute;
		this.amGoOffWorkHour = amGoOffWorkHour;
		this.amGoOffWorkMinute = amGoOffWorkMinute;
		this.pmGoToWorkHour = pmGoToWorkHour;
		this.pmGoToWorkMinute = pmGoToWorkMinute;
		this.pmGoOffWorkHour = pmGoOffWorkHour;
		this.pmGoOffWorkMinute = pmGoOffWorkMinute;
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

	/**
	 * 根据设置，得到一天的工作时间（毫秒）
	 * @return
	 */
	public long getOneDayDuration() {
		long am = getOneDayAmDuration();
		long pm = getOneDayPmDuration();
		return am + pm;
	}

	private long getOneDayPmDuration() {
		long j = 0;
		long minute = 0;
		if (pmGoOffWorkMinute > pmGoToWorkMinute) {
			minute = pmGoOffWorkMinute - pmGoToWorkMinute;
		} else {
			j = 1;
			minute = 60 + pmGoOffWorkMinute - pmGoToWorkMinute;
		}
		long hour = pmGoOffWorkHour - pmGoToWorkHour - j;
		return (60 * hour + minute) * 60 * 1000;
	}

	private long getOneDayAmDuration() {
		long j = 0;
		long minute = 0;
		if (amGoOffWorkMinute > amGoToWorkMinute) {
			minute = amGoOffWorkMinute - amGoToWorkMinute;
		} else {
			j = 1;
			minute = 60 + amGoOffWorkMinute - amGoToWorkMinute;
		}
		long hour = amGoOffWorkHour - amGoToWorkHour - j;
		return (60 * hour + minute) * 60 * 1000;
	}

	/**
	 * 
	 * @param day yyyy-MM-dd
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public String getAmStartStr(String day) {
		StringBuffer sb = new StringBuffer();
		if (!Strings.isBlank(day)) {
			sb.append(day).append(" ");
		}

		if (this.amGoToWorkHour < 10) {
			sb.append("0");
		}
		sb.append(this.amGoToWorkHour).append(":");

		if (this.amGoToWorkMinute < 10) {
			sb.append("0");
		}
		sb.append(this.amGoToWorkMinute).append(":00");

		return sb.toString();
	}

	/**
	 * 
	 * @param day yyyy-MM-dd
	 * @return yyyy-MM-dd HH:mm:ss
	 * @throws ParseException
	 */
	public Date getAmStart(String day) throws ParseException {
		String dateStr = getAmStartStr(day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(dateStr);
	}

	/**
	 * 
	 * @param day yyyy-MM-dd
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public String getAmEndStr(String day) {
		StringBuffer sb = new StringBuffer();
		if (!Strings.isBlank(day)) {
			sb.append(day).append(" ");
		}

		if (this.amGoOffWorkHour < 10) {
			sb.append("0");
		}
		sb.append(this.amGoOffWorkHour).append(":");

		if (this.amGoOffWorkMinute < 10) {
			sb.append("0");
		}
		sb.append(this.amGoOffWorkMinute).append(":00");

		return sb.toString();
	}

	/**
	 * 
	 * @param day yyyy-MM-dd
	 * @return yyyy-MM-dd HH:mm:ss
	 * @throws ParseException
	 */
	public Date getAmEnd(String day) throws ParseException {
		String dateStr = getAmEndStr(day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(dateStr);
	}

	/**
	 * 
	 * @param day yyyy-MM-dd
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public String getPmStartStr(String day) {
		StringBuffer sb = new StringBuffer();
		if (!Strings.isBlank(day)) {
			sb.append(day).append(" ");
		}

		if (this.pmGoToWorkHour < 10) {
			sb.append("0");
		}
		sb.append(this.pmGoToWorkHour).append(":");

		if (this.pmGoToWorkMinute < 10) {
			sb.append("0");
		}
		sb.append(this.pmGoToWorkMinute).append(":00");

		return sb.toString();
	}

	/**
	 * 
	 * @param day yyyy-MM-dd
	 * @return yyyy-MM-dd HH:mm:ss
	 * @throws ParseException
	 */
	public Date getPmStart(String day) throws ParseException {
		String dateStr = getPmStartStr(day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(dateStr);
	}

	/**
	 * 
	 * @param day yyyy-MM-dd
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public String getPmEndStr(String day) {
		StringBuffer sb = new StringBuffer();
		if (!Strings.isBlank(day)) {
			sb.append(day).append(" ");
		}

		if (this.pmGoOffWorkHour < 10) {
			sb.append("0");
		}
		sb.append(this.pmGoOffWorkHour).append(":");

		if (this.pmGoOffWorkMinute < 10) {
			sb.append("0");
		}
		sb.append(this.pmGoOffWorkMinute).append(":00");

		return sb.toString();
	}

	/**
	 * 
	 * @param day yyyy-MM-dd
	 * @return yyyy-MM-dd HH:mm:ss
	 * @throws ParseException
	 */
	public Date getPmEnd(String day) throws ParseException {
		String dateStr = getPmEndStr(day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(dateStr);
	}

}
