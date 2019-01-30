package com.ccw.config.controller;

import com.base.BaseController;
import com.xr.config.core.dto.ResultDto;
import com.xr.config.core.model.ConfigModel;
import com.xr.config.core.model.EnvModel;
import com.xr.config.core.service.IConfigService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {

  @Resource
  private IConfigService configService;

  @ResponseBody
  @RequestMapping(value = "/list")
  public ResultDto list(String envId) throws Exception {
    return super.successData(configService.queryAllConfig(new EnvModel(envId)));
  }

  @ResponseBody
  @RequestMapping(value = "/item")
  public ResultDto queryItem(String envId, String key) throws Exception {
    return super.successData(configService.queryConfigByKey(new EnvModel(envId), key));
  }

  @ResponseBody
  @RequestMapping(value = "/item/submit")
  public ResultDto editItem(String envId, ConfigModel newConfig) throws Exception {
    return super.successData(configService.addConfig(new EnvModel(envId), newConfig));
  }

  @ResponseBody
  @RequestMapping(value = "/item/upload")
  public ResultDto uploadItem(String envId, MultipartFile configFile) throws Exception {
    if(envId == null || envId.length() == 0){
      return this.failedMessage("请选择环境");
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(configFile.getInputStream()));
    if(br == null){
      return this.failedMessage("上传失败");
    }
    String[] items = null;
    String configItem = null;
    ConfigModel newConfig = null;
    while ((configItem = br.readLine()) != null) {
      if(configItem == null || configItem.trim().length() == 0 || configItem.startsWith("#")){
        continue; // filter comments
      }

      logger.info("create config item {}", configItem);
      items = configItem.split("=");
      if(items != null) {
        newConfig = new ConfigModel();
        newConfig.setKey(items[0].trim());
        newConfig.setValue( (items.length == 1 || items[1] == null || items[1].length() == 0) ? "" : items[1].trim());
        newConfig.setVersion(0);
        configService.addConfig(new EnvModel(envId), newConfig);
      }
    }

    return this.successMessage("上传成功");
  }

  @ResponseBody
  @RequestMapping(value = "/item/delete")
  public ResultDto deleteItem(String envId, String key) throws Exception {
    return super.successData(configService.deleteConfigByKey(new EnvModel(envId), key));
  }

  @ResponseBody
  @RequestMapping(value = "/enable")
  public ResultDto enableEnv(String envId) throws Exception {
    configService.enableEnv(envId);
    return super.successMessage(envId + " is available.");
  }

  @ResponseBody
  @RequestMapping(value = "/disable")
  public ResultDto disableEnv(String envId) throws Exception {
    configService.disableEnv(envId);
    return super.successMessage(envId + " is not available.");
  }

  @ResponseBody
  @RequestMapping(value = "/download")
  public HttpEntity<byte[]> downloadConfig(String fileName, String value) {

    HttpHeaders header = new HttpHeaders();
    byte[] res = value.getBytes();

    String name = null;

    try {
      name = URLEncoder.encode(fileName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // Don't worries.
    }

    header.set("Content-Disposition", "attachment; filename=" + name);
    header.setContentLength(res.length);
    return new HttpEntity<byte[]>(res, header);
  }

}
