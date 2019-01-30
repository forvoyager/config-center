package com.xr.config.core.aspect;

import com.xr.config.core.config.KeyValueStore;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 远程配置拦截
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-03-25
 */
@Aspect
public class RemoteConfigAspect {

  protected static final Logger LOGGER = LoggerFactory.getLogger(RemoteConfigAspect.class);

  @Pointcut(value = "execution(public * get*(..))")
  public void anyPublicMethod() {
  }

  @Around(value = "anyPublicMethod() && @annotation(com.xr.config.core.annotation.ConfigItem) && args(..)")
  public Object holdUpWhenQueryConfig(ProceedingJoinPoint pjp) throws Throwable {
    Object[] args = pjp.getArgs();

    /**
     * 优先从本地配置信息读取
     * 不存在则返回原始值
     */
    Object value = KeyValueStore.getInstance().getValue("");
    if (value != null) {
      LOGGER.debug("get config value : [{}] from key value store.", value);
      return value;
    }

    return pjp.proceed();
  }

}
