package com.xr.config.core.util;

import org.springframework.util.StringUtils;

public class StringUtil extends StringUtils{
  
  public static boolean isNotEmpty(Object target) {
    return !isEmpty(target);
  }

}
