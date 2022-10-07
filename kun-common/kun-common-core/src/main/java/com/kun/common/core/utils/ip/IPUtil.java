package com.kun.common.core.utils.ip;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ip工具类
 *
 * @author gzc
 * @since 2022/10/7 18:09
 */
public class IPUtil {

    /**
     * IP归属地查询
     */
    private static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";

    private static final String UNKNOWN = "unknown";

    /**
     * 获取ip地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }


    /**
     * 根据ip获取详细地址
     */
    public static String getCityInfo(String ip) {
        String api = String.format(IP_URL, ip);
        JSONObject jsonObject = JSON.parseObject(HttpUtil.get(api));
        return jsonObject.getString("addr");
    }


//	/**
//	 * 获取浏览器
//	 * @param request
//	 * @return
//	 */
//	public static String getBrowser(HttpServletRequest request) {
//		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
//		Browser browser = userAgent.getBrowser();
//		return browser.getName();
//	}
}
