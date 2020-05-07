package com.linkallcloud.um.constant;

public interface Consts {
	
	public static final String CONFIG_KH_LOGO = "logo";
	public static final String CONFIG_KH_PERMISSION_ORG = "enable_org_permission";
	public static final String CONFIG_KH_PERMISSION_AREA = "enable_area_permission";
	public static final String CONFIG_KH_AREAS = "area_roots";
	
	public static final String ORG_TREE_TYPE_SELF = "SelfTree";//组织树中紧包含本单位的部门节点
	public static final String ORG_TREE_TYPE_COMPANY = "CompanyTree";//组织树中包含本单位的部门节点及包含子单位的根节点
	public static final String ORG_TREE_TYPE_FULL = "FullTree";//组织树中包含本单位的部门节点及包含子单位的所有节点
	public static final String ORG_TREE_TYPE_FULL_COMPANY = "FullTreeCompany";//组织树中包含所有子单位节点，不含部门节点

}
