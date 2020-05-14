package com.linkallcloud.um.constant;

public interface Consts {
	
	public static final String CONFIG_LOGO = "logo";//kh,yw
	public static final String CONFIG_PERMISSION_ORG = "enable_org_permission";//kh,yw
	public static final String CONFIG_PERMISSION_AREA = "enable_area_permission";//kh,yw
	public static final String CONFIG_AREAS = "area_roots";//kh,yw
	public static final String CONFIG_MANAGE_DEPARTMENT = "enable_m_dep";//kh,yw
	public static final String CONFIG_ZZD = "enable_zzd";//kh,yw
	public static final String CONFIG_COMPANY_CLASS = "company_class";//yw //kh_xx:普通单位；kh_qy：企业单位
	
	public static final String ORG_TREE_TYPE_SELF = "SelfTree";//组织树中紧包含本单位的部门节点
	public static final String ORG_TREE_TYPE_COMPANY = "CompanyTree";//组织树中包含本单位的部门节点及包含子单位的根节点
	public static final String ORG_TREE_TYPE_FULL = "FullTree";//组织树中包含本单位的部门节点及包含子单位的所有节点
	public static final String ORG_TREE_TYPE_FULL_COMPANY = "FullTreeCompany";//组织树中包含所有子单位节点，不含部门节点

}
