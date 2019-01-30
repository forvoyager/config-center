package com.xr.ccw.config.controller;

import com.xr.base.BaseController;
import com.xr.config.core.constant.Constants;
import com.xr.config.core.dto.ResultDto;
import com.xr.config.core.model.EnvModel;
import com.xr.config.core.service.IConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/env")
public class EnvController extends BaseController {

  @Resource
  private IConfigService configService;

  @ResponseBody
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public ResultDto list() throws Exception {
    return super.successData(configService.listAllEnvWhenKeyLike(Constants.ENV_PREFIX));
  }

  @ResponseBody
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public ResultDto add(EnvModel env) throws Exception {
    Assert.hasLength(env.getDisplayName(), "名称必填");
    Assert.hasLength(env.getUrl(), "地址必填");
    env.setEnvId(Constants.ENV_PREFIX + env.getDisplayName().hashCode());
    configService.addEnv(env);
    return super.successMessage("添加成功");
  }
}
