package com.xr.base.filter;

import com.xr.config.core.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;

/**
 * @author Arnold Yand
 * @summary AccessFilter.java
 */
@Component
@WebFilter(urlPatterns = "/*",filterName = "accessFilter")
public class AccessFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(AccessFilter.class);

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    RequestWrapper req = new RequestWrapper(((HttpServletRequest)request));

    try {
      // 只允许本地访问后台
      if( !IpUtil.getLocalAvailableIP().equals( IpUtil.getIpAddr( req )) ){
        throw new IllegalAccessException("只允许本机访问后台");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    long startTime = System.currentTimeMillis();
    String uri = req.getRequestURI();
    log.info("call [{}] from [{}]", uri, IpUtil.getIpAddr(req));
    chain.doFilter(request, response);
    log.info("call [{}] end, cost {}ms", uri, System.currentTimeMillis() - startTime);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  private class RequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> parameters;

    public RequestWrapper(HttpServletRequest request) {
      super(request);
      parameters = request.getParameterMap();
    }

    public void setParameter(String name, String value) {
      this.parameters.put(name, new String[]{value});
    }

    @Override
    public String getParameter(String name) {
      return getParameterWithDefault(name, null);
    }

    public String getParameterWithDefault(String name, String defaultValue) {
      String[] values = parameters.get(name);
      return (values == null || values.length == 0) ? defaultValue : values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
      return this.parameters;
    }
  }
}
