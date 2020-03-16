package com.linkallcloud.um.excel;

import java.io.Serializable;
import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.beans.BeanUtils;

public class LogExport implements Serializable {
	private static final long serialVersionUID = -4647646015401525213L;

	@Excel(name = "操作时间",exportFormat="yyyy-MM-dd HH:mm:ss")
	private Date operateTime; // 操作时间
	@Excel(name = "操作者")
	private String operatorAccount;// 操作者的登录名
	@Excel(name = "操作模块")
	private String module;// 操作的模块
	@Excel(name = "操作内容")
	private String operateDesc;// 操作描述
	@Excel(name = "操作结果")
	private int operateResult;// 操作结果:成功/失败
	@Excel(name = "耗时(ms)")
	private Long costTime;// 操作花费时间
	@Excel(name = "操作者IP")
	private String ip;// 操作者的登陆ip
	@Excel(name = "操作者URL")
	private String url;// url
	@Excel(name = "错误信息")
	private String errorMessage;// 失败的error信息

	public LogExport() {
		super();
	}

	public LogExport(Object o) {
		super();
		if (o != null) {
			try {
				BeanUtils.copyProperties(o, this);
			} catch (Throwable e) {
			}
		}
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperatorAccount() {
		return operatorAccount;
	}

	public void setOperatorAccount(String operatorAccount) {
		this.operatorAccount = operatorAccount;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getOperateDesc() {
		return operateDesc;
	}

	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}

	public int getOperateResult() {
		return operateResult;
	}

	public void setOperateResult(int operateResult) {
		this.operateResult = operateResult;
	}

	public Long getCostTime() {
		return costTime;
	}

	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
