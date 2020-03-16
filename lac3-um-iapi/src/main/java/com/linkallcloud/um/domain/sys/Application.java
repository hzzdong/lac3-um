package com.linkallcloud.um.domain.sys;

import com.linkallcloud.core.domain.Domain;
import com.linkallcloud.core.domain.annotation.ShowName;
import com.linkallcloud.core.dto.Tree;
import com.linkallcloud.core.principal.AccountMapping;

@ShowName("应用")
public class Application extends Domain {
    private static final long serialVersionUID = -588316821409393023L;

    private String code;
    private String name;

    private String url;
    private int sort;
    private int type;
    private String ico;

    private int screenType;//适合屏幕尺寸：参考：com.linkallcloud.um.enums.ScreenType
    private int mappingType;//账号映射类型，参考：com.linkallcloud.core.principal.AccountMapping
    private String logout;//logout url

    /* 接口安全设置 */
    private String host;// 应用host（IP或域名白名单）
    private long timeout;// 超时时间
    private String messageEncAlg;// 消息加解密算法，暂时支持AES。
    private String messageEncKey;// 加密的秘钥
    // private String signatureIdentity;// 签名者ID(appKey)//用code替代
    private String signatureAlg;// 签名算法(SHA1/MD5)
    private String signatureKey;// 签名密钥
    /* end 接口安全设置 */

    /* 账号映射模式认证接口设置:
        调用时机
        根据以上设置，当用户首次访问此应用时，SSO会发起账户绑定操作。用户在绑定界面上输入账号信息后，SSO会以rest方式调用应用的账号认证接口
        接口调用方式
        POST调用：账号认证接口地址?loginName=XXX&password=XXX&time=XXX$sign=XXX
        参数说明
        （1）password=原文或者MD5(原文)
        （2）time=当前时间new Date().getTime()
        （3）sign=MD5/SHA1(loginName+password+time+签名密钥)
        接口返回
        接口返回JSON结果如下：
        （1）成功：{code:"0",message:"成功"}
        （2）失败：{code:"XXX",message:"失败原因"}
    */
    private String authAddr;//认证接口地址
    private Integer authPassMode;//密码模式，参考：com.linkallcloud.core.enums.EnumBase.PasswordMode
    private Integer authSignAlg;//签名算法,参考：com.linkallcloud.core.enums.EnumBase.SignAlg
    private String authSignKey;//签名密钥
    /* end 账号映射模式认证接口设置 */

    private String remark;

    /*
     * 以下非数据库字段
     */
    private Long ownerId;
    private String ownerUuid;
    private String ownerName;

    public Application() {
        super();
        this.mappingType = AccountMapping.Unified.getCode();
    }

    public Application(Long id, String uuid) {
        super(id, uuid);
        this.mappingType = AccountMapping.Unified.getCode();
    }

    public Application(Long id) {
        super(id);
        this.mappingType = AccountMapping.Unified.getCode();
    }

    public void desensitization() {
        this.host = null;
        this.timeout = 0;
        this.messageEncAlg = null;
        this.messageEncKey = null;
        this.signatureAlg = null;
        this.signatureKey = null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public Tree toTreeNode() {
        Tree node = new Tree(this.getId() == null ? null : this.getId().toString(), this.getUuid(), null,
                this.getName(), this.getCode(), String.valueOf(this.getType()), this.getStatus());
        node.addAttribute("alias", this.getClass().getSimpleName());
        return node;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Tree toMenuRoot() {
        Tree root = this.toTreeNode();
        root.setId("APP-" + this.getId());
        root.setpId(null);
        root.setOpen(true);
        return root;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getMessageEncAlg() {
        return messageEncAlg;
    }

    public void setMessageEncAlg(String messageEncAlg) {
        this.messageEncAlg = messageEncAlg;
    }

    public String getMessageEncKey() {
        return messageEncKey;
    }

    public void setMessageEncKey(String messageEncKey) {
        this.messageEncKey = messageEncKey;
    }

//	public String getSignatureIdentity() {
//		return signatureIdentity;
//	}
//
//	public void setSignatureIdentity(String signatureIdentity) {
//		this.signatureIdentity = signatureIdentity;
//	}

    public String getSignatureAlg() {
        return signatureAlg;
    }

    public void setSignatureAlg(String signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public boolean isValid() {
        return this.status == 0;
    }

    public int getMappingType() {
        return mappingType;
    }

    public void setMappingType(int mappingType) {
        this.mappingType = mappingType;
    }

    public int getScreenType() {
        return screenType;
    }

    public void setScreenType(int screenType) {
        this.screenType = screenType;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }

    public String getAuthAddr() {
        return authAddr;
    }

    public void setAuthAddr(String authAddr) {
        this.authAddr = authAddr;
    }

    public Integer getAuthPassMode() {
        return authPassMode;
    }

    public void setAuthPassMode(Integer authPassMode) {
        this.authPassMode = authPassMode;
    }

    public Integer getAuthSignAlg() {
        return authSignAlg;
    }

    public void setAuthSignAlg(Integer authSignAlg) {
        this.authSignAlg = authSignAlg;
    }

    public String getAuthSignKey() {
        return authSignKey;
    }

    public void setAuthSignKey(String authSignKey) {
        this.authSignKey = authSignKey;
    }
}
