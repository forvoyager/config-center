package com.xr.base;

import com.xr.ccw.user.model.User;
import com.xr.config.core.constant.Constants;
import com.xr.config.core.dto.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arnold Yand
 * @summary
 * @time 2018/5/11 18:54
 */
public class BaseController {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected HttpServletRequest request;
  protected HttpServletResponse response;

  @ModelAttribute
  public void initReqAndRep(HttpServletRequest request, HttpServletResponse response) {
    this.request = request;
    this.response = response;
  }

  protected User getSessionUser() {
    return (User) request.getSession().getAttribute(Constants.USER_KEY);
  }

  protected void setSessionUser(User user) {
    request.getSession().setAttribute(Constants.USER_KEY, user);
  }

  protected void removeSessionUser() {
    request.getSession().removeAttribute(Constants.USER_KEY);
  }

  protected Long getSessionUserId() {
    User user = this.getSessionUser();
    return user == null ? null : user.getUserId();
  }

  protected ResultDto success(String msg, Object obj) {
    return ResultDto.success(msg, obj);
  }

  protected ResultDto successMessage(String msg) {
    return success(msg, null);
  }

  protected ResultDto successData(Object obj) {
    return success(null, obj);
  }

  protected ResultDto failed(String msg, Object obj) {
    return ResultDto.failed(msg, obj);
  }

  protected ResultDto failedMessage(String msg) {
    return failed(msg, null);
  }

  protected ResultDto failedData(Object obj) {
    return failed(null, obj);
  }
}
