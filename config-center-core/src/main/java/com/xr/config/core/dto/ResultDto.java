package com.xr.config.core.dto;

import com.xr.config.core.enums.ResultStatus;
import com.xr.config.core.util.StringUtil;

import java.io.Serializable;

/**
 * @author Arnold Yand
 * @summary 通用返回结果
 * @time 2018/4/7 11:50
 */
public class ResultDto<T> implements Serializable{

  private static final long serialVersionUID = 20180412102000L;

  // @see ResultStatus
  private int code;
  private String message;

  // response body
  private T data;

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

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public static <T> ResultDto success(String message, T data){
    ResultDto success = new ResultDto();
    success.setCode(ResultStatus.SUCCESS.getCode());
    success.setMessage(StringUtil.isEmpty(message) ? ResultStatus.SUCCESS.getMessage() : message);
    success.setData( data);
    return success;
  }

  public static <T> ResultDto failed(String message, T data){
    ResultDto success = new ResultDto();
    success.setCode(ResultStatus.FAILED.getCode());
    success.setMessage(StringUtil.isEmpty(message) ? ResultStatus.FAILED.getMessage() : message);
    success.setData( data);
    return success;
  }
}
