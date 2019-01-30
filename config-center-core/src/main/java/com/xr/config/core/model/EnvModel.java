package com.xr.config.core.model;

import java.io.Serializable;

/**
 * @author Arnold Yand
 * @summary
 * @time 2018/6/17 9:49
 */
public class EnvModel implements Serializable{

  // 是否可用
  private boolean available;

  // id
  private String envId;

  // 显示名称
  private String displayName;

  // 地址ip:port
  private String url;

  // 描述
  private String description;

  public EnvModel(){}

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public EnvModel(String envId){
    this.envId = envId;
  }

  public String getEnvId() {
    return envId;
  }

  public void setEnvId(String envId) {
    this.envId = envId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
