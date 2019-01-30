package com.xr.config.core.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * @author Arnold Yand
 * @summary com.common.IpUtil.java
 */
public class IpUtil {

  public static final String ANYHOST = "0.0.0.0";

  public static final String LOCALHOST = "127.0.0.1";

  public static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

  public static String getIpAddr(HttpServletRequest request) {

    String ipAddress = request.getHeader("x-forwarded-for");

    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("Proxy-Client-IP");
    }

    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("WL-Proxy-Client-IP");
    }

    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getRemoteAddr();
      if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
        // 根据网卡取本机配置的IP
        try {
          ipAddress = getLocalAvailableIP();
        } catch (Exception e) { }
      }
    }

    // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
    if (ipAddress != null && ipAddress.indexOf(",") > 0) {
      ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
    }

    return ipAddress;
  }

  public static String getLocalAvailableIP() throws Exception {
    InetAddress localAddress = InetAddress.getLocalHost();
    if (isValidAddress(localAddress)) {
      return localAddress.getHostAddress();
    }

    // 获取本机所有网卡
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    if (interfaces != null) {
      NetworkInterface network = null;
      while (interfaces.hasMoreElements()) {
        network = interfaces.nextElement();

        // 获取网卡绑定的IP地址
        Enumeration<InetAddress> addresses = network.getInetAddresses();
        if (addresses != null) {
          while (addresses.hasMoreElements()) {
            InetAddress address = addresses.nextElement();
            if (isValidAddress(address)) {
              return address.getHostAddress();
            }
          }
        }
      }
    }

    throw new RuntimeException("no available IP address.");
  }

  private static boolean isValidAddress(InetAddress address) {
    if (address == null || address.isLoopbackAddress())
      return false;
    String name = address.getHostAddress();
    return (name != null
      && !ANYHOST.equals(name)
      && !LOCALHOST.equals(name)
      && IP_PATTERN.matcher(name).matches());
  }
}
