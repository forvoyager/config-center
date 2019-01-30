package com.xr.config.api;

import base.BaseController;
import com.xr.config.core.dto.ResultDto;
import com.xr.config.core.service.IConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("")
public class ConfigController extends BaseController {

  @Resource
  private IConfigService configService;

  @ResponseBody
  @RequestMapping(value = "/load/available/env", method = RequestMethod.GET)
  public ResultDto list() throws Exception {
    return super.successData(configService.queryAvailableConfig());
  }

}
