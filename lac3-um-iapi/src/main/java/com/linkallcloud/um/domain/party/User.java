package com.linkallcloud.um.domain.party;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.alibaba.fastjson.annotation.JSONField;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.util.Domains;

@ShowName("用户")
public abstract class User extends Party {
	private static final long serialVersionUID = -291766963965804339L;

	private Long companyId;

	private String nickName;
	private String mobile;
	private String email;
	/** 身份证号 */
	private String idCard;
	private String sex;

	@JSONField(format = "yyyy-MM-dd")
	private Date birthday;

	/** 账号 */
	@NotBlank(message = "账号不能为空")
	private String account;
	/** 密码 */
	@JSONField(serialize = false)
	private String password;
	@JSONField(serialize = false)
	private String salt;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

	private String jobPosition;// 岗位
	private String job;// 职务
	private int sms;// 短信提醒，1：需要提醒，0：不需要提醒

	private String zwuid;// 政务服务网用户ID
	private String dduid;// 钉钉用户ID
	private int ddStatus;// 钉钉用户状态，0：未同步，1：同步成功，2：浙政钉用户同步失败
	
	private int dyType;//是否党员，1：是，0：否
	@JSONField(format = "yyyy-MM-dd")
	private Date rdDate;//入党日期
	private String dorg;//所在党组织

	/*
	 * 以下字段为查询字段
	 */
	private String companyName;
	private List<Long> roleIds;// 新增和编辑界面上选中中角色
	private boolean roleEnabled;// 编辑时否维护用户的角色
	private Long jzCompanyId;//兼职公司id，用于兼职目标公司管理员编辑此用户信息时记录目标公司id
	private String jzCompanyName;

	/** 原密码 */
	@JSONField(serialize = false)
	private String oldpassword;

	public User() {
		super();
		roleEnabled = false;
	}

	public User(String name, String account, String mobile, String password) {
		this();
		this.setName(name);
		this.setAccount(account);
		this.setMobile(mobile);
		this.setPassword(password);
	}

	public void desensitization() {
		this.password = null;
		this.salt = null;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Override
	public String codeTag() {
		return "@";
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getUserType() {
		return this.getClass().getSimpleName().substring(0, 2);
	}

	public boolean isAdmin() {
		return Domains.USER_ADMIN == this.getType();
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDduid() {
		return dduid;
	}

	public void setDduid(String dduid) {
		this.dduid = dduid;
	}

	public int getDdStatus() {
		return ddStatus;
	}

	public void setDdStatus(int ddStatus) {
		this.ddStatus = ddStatus;
	}

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public int getSms() {
		return sms;
	}

	public void setSms(int sms) {
		this.sms = sms;
	}

	public String getZwuid() {
		return zwuid;
	}

	public void setZwuid(String zwuid) {
		this.zwuid = zwuid;
	}

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public boolean isRoleEnabled() {
		return roleEnabled;
	}

	public void setRoleEnabled(boolean roleEnabled) {
		this.roleEnabled = roleEnabled;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public Long getRootDepartmentId() {
		return Domains.parseMyRootDepartmentId(getCode());
	}

	public int getDyType() {
		return dyType;
	}

	public void setDyType(int dyType) {
		this.dyType = dyType;
	}

	public Date getRdDate() {
		return rdDate;
	}

	public void setRdDate(Date rdDate) {
		this.rdDate = rdDate;
	}

	public String getDorg() {
		return dorg;
	}

	public void setDorg(String dorg) {
		this.dorg = dorg;
	}

	public Long getJzCompanyId() {
		return jzCompanyId;
	}

	public void setJzCompanyId(Long jzCompanyId) {
		this.jzCompanyId = jzCompanyId;
	}

	public String getJzCompanyName() {
		return jzCompanyName;
	}

	public void setJzCompanyName(String jzCompanyName) {
		this.jzCompanyName = jzCompanyName;
	}

}
