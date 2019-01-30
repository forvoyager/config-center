package com.xr.config.core.enums;

/**
 * @author Arnold Yand
 * @summary
 * @time 2018/4/7 10:54
 */
public enum ResultStatus {
  FAILED(99, "系统繁忙，请稍后重试。"),
  SUCCESS(100, "成功")
  ;

  private ResultStatus(int code, String message){
    this.code = code;
    this.message = message;
  }

  private int code;
  private String message;
  private Object data;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
