package com.xr.ccw.user.model;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-05-11
 */
public class User {

  private static final long serialVersionUID = 102L;

  /**
   * 用户ID
   */
  private Long userId;
  /**
   * 用户名
   */
  private String userName;
  /**
   * 邮箱
   */
  private String email;
  /**
   * 手机号
   */
  private String mobileNumber;
  /**
   * 加密盐
   */
  private String encryptSalt;
  /**
   * 密码
   */
  private String password;
  /**
   * 用户角色
   */
  private Integer userRole;
  /**
   * 注册时间
   */
  private Long registryTime;
  /**
   * 头像
   */
  private String portrait;
  /**
   * 创建时间（秒）
   */
  private Long createTime;
  /**
   * 最后更新时间（秒）
   */
  private Long updateTime;
  /**
   * 版本号，每次更新+1
   */
  private Integer version;
  /**
   * 扩展信息
   */
  private String feature;


  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getEncryptSalt() {
    return encryptSalt;
  }

  public void setEncryptSalt(String encryptSalt) {
    this.encryptSalt = encryptSalt;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public Integer getUserRole() {
    return userRole;
  }

  public void setUserRole(Integer userRole) {
    this.userRole = userRole;
  }

  public Long getRegistryTime() {
    return registryTime;
  }

  public void setRegistryTime(Long registryTime) {
    this.registryTime = registryTime;
  }

  public String getPortrait() {
    return portrait;
  }

  public void setPortrait(String portrait) {
    this.portrait = portrait;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getFeature() {
    return feature;
  }

  public void setFeature(String feature) {
    this.feature = feature;
  }

  @Override
  public String toString() {
    return "User{" +
            ", userId=" + userId +
            ", userName=" + userName +
            ", email=" + email +
            ", mobileNumber=" + mobileNumber +
            ", encryptSalt=" + encryptSalt +
            ", password=" + password +
            ", userRole=" + userRole +
            ", registryTime=" + registryTime +
            ", portrait=" + portrait +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", version=" + version +
            ", feature=" + feature +
            "}";
  }
}
