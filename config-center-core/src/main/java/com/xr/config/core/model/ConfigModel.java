package com.xr.config.core.model;

import java.io.Serializable;

/**
 * @author Arnold Yand
 * @summary 配置项信息
 * @time 2018/6/17 14:16
 */
public class ConfigModel implements Serializable{
  private String key;
  private String value;
  private int version;

  public ConfigModel(){}

  public ConfigModel(String key, String value){
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }
}
