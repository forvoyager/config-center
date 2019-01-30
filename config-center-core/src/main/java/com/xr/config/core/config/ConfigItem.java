package com.xr.config.core.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * <p>
 * 配置项信息
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-04-17
 */
public class ConfigItem {

  private String key;
  private Object value;

  // Field
  private Field field;
  private Method setMethod;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public void setField(Field field) {
    this.field = field;
  }

  public void setSetMethod(Method setMethod) {
    this.setMethod = setMethod;
  }

  public Object setValueForField(Object value) throws Exception {

    if (setMethod != null) {
      setMethod.invoke(null, value);
    } else {
      field.set(null, value);
    }

    return value;
  }

  public boolean isStatic() {
    return Modifier.isStatic(field.getModifiers());
  }
}
