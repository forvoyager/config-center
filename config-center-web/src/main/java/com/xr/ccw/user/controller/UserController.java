package com.xr.ccw.user.controller;

import com.xr.base.BaseController;
import com.xr.ccw.user.dto.SessionUser;
import com.xr.ccw.user.model.User;
import com.xr.config.core.dto.ResultDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-05-11
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

  @ResponseBody
  @RequestMapping(value = "/account/session", method = RequestMethod.GET)
  public ResultDto getSession() {
    User user = super.getSessionUser();
    if (user == null) {
      return super.failedMessage("请先登录");
    } else {
      return super.successData(user);
    }

  }

  @ResponseBody
  @RequestMapping(value = "/account/login", method = RequestMethod.POST)
  public ResultDto login(String name, String password) {

    // TODO 登陆

    SessionUser user = new SessionUser();
    user.setUserId(666L);
    user.setUserName("utttupp");
    super.setSessionUser(user);

    return super.successData(user);
  }

  @ResponseBody
  @RequestMapping(value = "/account/logout", method = RequestMethod.GET)
  public ResultDto logout(HttpServletRequest request) {
    super.removeSessionUser();
    return super.successMessage("OK");
  }

}
