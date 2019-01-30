package com.xr.config.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;


/**
 * @author Arnold Yand
 */
public class HttpUtils {

  protected static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

  protected static final String HTTP_REQUEST_LOG_PATTERN = "call {}, with parameter {}, and cost is {}ms, http status is {}, result is {}";
  protected static final String HTTP_METHOD_POST = "POST";
  protected static final String HTTP_METHOD_GET = "GET";

  public static final String DEFAULT_CHARSET = "UTF-8";

  public static String post(String url) throws Exception{
    return post(url, null);
  }

  public static String post(String url, Map<String, Object> params) throws Exception{
    HttpURLConnection conn = getConnection(url, HTTP_METHOD_POST);

    if (params != null && params.size() > 0) {
      StringBuffer reqParams = new StringBuffer();
      for (String key : params.keySet()) {
        reqParams.append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), DEFAULT_CHARSET)).append("&");
      }

      OutputStream outwritestream = conn.getOutputStream();
      outwritestream.write(reqParams.toString().getBytes(DEFAULT_CHARSET));
      outwritestream.flush();
      outwritestream.close();
    }

    long startTime = System.currentTimeMillis();

    String result = parseResponse(conn.getInputStream());
    conn.disconnect();

    logger.info(HTTP_REQUEST_LOG_PATTERN, url, JSONUtil.toJSONString(params), (System.currentTimeMillis() - startTime), conn.getResponseCode(), result);

    return result;
  }

  public static String get(String url) throws Exception{
    return get(url, null);
  }

  public static String get(String url, Map<String, Object> params) throws Exception{

    StringBuffer finalUrl = new StringBuffer();
    if (params != null && params.size() > 0) {
      for (String key : params.keySet()) {
        finalUrl.append("&").append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), DEFAULT_CHARSET));
      }
      finalUrl.replace(0, 1, "?");
    }
    finalUrl.insert(0, url);

    HttpURLConnection conn = getConnection(finalUrl.toString(), HTTP_METHOD_GET);

    long startTime = System.currentTimeMillis();
    String result = parseResponse(conn.getInputStream());
    conn.disconnect();

    logger.info(HTTP_REQUEST_LOG_PATTERN, finalUrl.toString(), JSONUtil.toJSONString(params), (System.currentTimeMillis() - startTime), conn.getResponseCode(), result);

    return result;
  }

  private static HttpURLConnection getConnection(String url, String type) throws Exception {
    type = StringUtil.isEmpty(type) ? "GET": type.toUpperCase();
    HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
    conn.setRequestProperty("Charset", DEFAULT_CHARSET);
    conn.setRequestProperty("accept", "*/*");
    conn.setRequestProperty("connection", "Keep-Alive");
    conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
    if(HTTP_METHOD_POST.equals(type)){
      conn.setDoOutput(true);
    }
    conn.setDoInput(true);
    conn.setUseCaches(false);
    conn.setRequestMethod(type);
    conn.setInstanceFollowRedirects(true);
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setRequestProperty("accept","*/*"); // */* application/json
    conn.connect();
    return conn;
  }

  private static String parseResponse(InputStream responseStream){
    StringBuilder result = new StringBuilder();

    BufferedReader in = new BufferedReader(new InputStreamReader(responseStream));
    try {
      String line = null;
      while ((line = in.readLine()) != null) {
        result.append(URLDecoder.decode(line, DEFAULT_CHARSET));
      }
    } catch (IOException e) {
      logger.error("parse response occur exception.", e);
    } finally {
      if(in != null){
        try {
          in.close();
        } catch (Exception e) { }
      }
    }

    return result.toString();
  }
}
