package com.imooc.utils;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Cookie 工具类
 */
public final class CookieUtils {

    final static Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    /**
     * 获取Cookie的值，获取编码形式（不解码）
     *
     * @param request    请求对象，用于获取Cookie信息
     * @param cookieName Cookie的名称，用于指定要获取哪个Cookie
     * @return 返回指定名称的Cookie的值，如果找不到或发生异常则返回空字符串
     * <br>
     * Note: 该方法通过调用另一个重载的getCookieValue方法实现，isDecoder 为 false，表示获取的Cookie值不进行解码
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }


    /**
     * 根据Cookie名称获取Cookie值
     *
     * @param request    HttpServletRequest对象，用于获取Cookie信息
     * @param cookieName 需要获取的Cookie的名称
     * @param isDecoder  是否对Cookie值进行解码
     * @return 返回Cookie的值，如果未找到或出现异常，返回null
     * <p>
     * 此方法用于从HTTP请求中提取指定名称的Cookie值如果isDecoder为true，将对Cookie值进行URL解码
     * 使用UTF-8编码如果未找到对应名称的Cookie或发生解码异常，方法将返回null
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        // 验证request是否为null
        if (request == null || cookieName == null || cookieName.isEmpty()) {
            return null;
        }

        // 获取请求中的所有Cookie
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null) {
            return null;
        }

        // 初始化Cookie值为null，待后续查找
        String cookieValue = null;
        try {
            // 遍历Cookie数组，寻找匹配的Cookie名称
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(cookieName)) {
                    // 根据isDecoder参数决定是否解码Cookie值
                    if (isDecoder) {
                        cookieValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
                    } else {
                        cookieValue = cookie.getValue();
                    }
                    break; // 明确表示找到后退出循环
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to decode cookie value", e);
            return null;
        }

        return cookieValue;
    }


    /**
     * 获取指定名称的Cookie值。
     *
     * @param request      HttpServletRequest对象
     * @param cookieName   需要获取的Cookie名称
     * @param encodeString 解码字符串所使用的字符集，默认为UTF-8
     * @return 解码后的Cookie值，若未找到或发生错误则返回null
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        // 检查request是否为null
        if (request == null || cookieName == null) {
            return null;
        }

        Cookie[] cookieList = request.getCookies();
        if (cookieList == null) {
            return null;
        }

        String retValue = null;
        String encoding = encodeString != null ? encodeString : "UTF-8"; // 使用默认编码

        try {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookie.getValue(), encoding);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported Encoding Exception: {}", e.getMessage());
        }

        return retValue;
    }

    /**
     * 设置Cookie的值 默认情况下，Cookie的生效时间与浏览器的生命周期一致，即浏览器关闭后Cookie失效，且不进行编码
     *
     * @param request     请求对象，用于获取请求信息
     * @param response    响应对象，用于设置Cookie
     * @param cookieName  Cookie的名称
     * @param cookieValue Cookie的值
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String cookieName,
                                 String cookieValue) {
        // 调用重载的setCookie方法，将cookie生效时间设置为-1，表示默认行为
        setCookie(request, response, cookieName, cookieValue, -1);
    }


    /**
     * 设置Cookie的值，在指定时间内生效，但不编码
     *
     * @param request      请求对象，用于获取客户端信息
     * @param response     响应对象，用于向客户端发送数据
     * @param cookieName   Cookie的名称，用于标识Cookie
     * @param cookieValue  Cookie的值，用于存储信息
     * @param cookieMaxAge Cookie的最大生命周期，以秒为单位，用于控制Cookie的存活时间
     * @Description: 设置Cookie的值 在指定时间内生效,但不编码
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String cookieName,
                                 String cookieValue,
                                 int cookieMaxAge) {
        // 调用重载的setCookie方法，允许指定是否编码
        setCookie(request, response, cookieName, cookieValue, cookieMaxAge, false);
    }


    /**
     * 设置Cookie的值，并指定是否编码。此方法简化了Cookie的设置过程， 没有指定过期时间，浏览器关闭后 Cookie 失效。
     * <br>
     * Cookie主要存储在客户端，可用于会话管理等场景。通过设置路径，可以限制只有指定路径下的页面能访问该Cookie。
     *
     * @param request     请求对象，用于获取Cookie。
     * @param response    响应对象，用于设置Cookie到客户端。
     * @param cookieName  Cookie的名称。
     * @param cookieValue Cookie的值。
     * @param isEncode    是否对Cookie值进行URL编码，以防止特殊字符导致的问题。
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String cookieName,
                                 String cookieValue,
                                 boolean isEncode) {
        // 调用重载的setCookie方法，设置Cookie并指定是否编码
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }


    /**
     * 设置Cookie的值，在指定时间内生效，并根据需要进行编码。
     *
     * @param request      请求对象，用于获取客户端信息等。
     * @param response     响应对象，用于向客户端发送数据等。
     * @param cookieName   Cookie的名称。
     * @param cookieValue  Cookie的值。
     * @param cookieMaxAge Cookie的有效时间（以秒为单位）。
     * @param isEncode     是否对Cookie值进行编码。
     * @Description: 设置Cookie的值，在指定时间内生效，编码参数。
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String cookieName,
                                 String cookieValue,
                                 int cookieMaxAge,
                                 boolean isEncode) {
        // 调用实际设置Cookie的私有方法
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, isEncode);
    }


    /**
     * 设置Cookie的值，在指定时间内生效，并使用指定的编码方式对Cookie值进行编码。
     *
     * @param request      当前的HttpServletRequest对象，用于获取请求信息。
     * @param response     当前的HttpServletResponse对象，用于设置响应头信息。
     * @param cookieName   要设置的Cookie的名称。
     * @param cookieValue  要设置的Cookie的值。
     * @param cookieMaxAge Cookie的有效期，以秒为单位。如果为负值，则表示Cookie是临时的，关闭浏览器后即消失。
     * @param encodeString 用于对Cookie值进行编码的字符串。可以为空，如果不为空，则使用该字符串对Cookie值进行编码。
     * @Description: 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
     */
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String cookieName,
                                 String cookieValue,
                                 int cookieMaxAge,
                                 String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, encodeString);
    }


    /**
     * 删除指定名称的Cookie
     * 此方法通过设置一个过期时间来实现Cookie的删除，即将其过期时间设置为负值，
     * 浏览器会将其视为过期，从而达到删除Cookie的效果
     *
     * @param request    请求对象，用于获取Cookie信息
     * @param response   响应对象，用于设置Cookie信息
     * @param cookieName 待删除的Cookie的名称
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        // 调用设置Cookie的方法，将Cookie的值设为空，过期时间为负值，表示立即过期
        doSetCookie(request, response, cookieName, null, -1, false);
    }


    /**
     * 设置Cookie的值，并使其在指定时间内生效
     *
     * @param request      请求对象，用于获取客户端信息
     * @param response     响应对象，用于向客户端发送数据
     * @param cookieName   Cookie的名称
     * @param cookieValue  Cookie的值
     * @param cookieMaxAge cookie生效的最大秒数
     * @param isEncode     是否对Cookie值进行编码
     * @Description: 设置Cookie的值，并使其在指定时间内生效
     */
    private static void doSetCookie(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String cookieName,
                                    String cookieValue,
                                    int cookieMaxAge,
                                    boolean isEncode) {
        // 根据isEncode参数决定是否对Cookie值进行UTF-8编码
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, isEncode ? "utf-8" : null);
    }


    /**
     * 设置HTTP响应的Cookie
     *
     * @param request      HTTP请求对象，用于获取请求的上下文路径和避免null pointer exception
     * @param response     HTTP响应对象，用于添加Cookie
     * @param cookieName   Cookie的名称
     * @param cookieValue  Cookie的值，如果为null将被设置为空字符串
     * @param cookieMaxAge Cookie的最大年龄（秒），如果大于0则设置Cookie的maxAge属性
     * @param encodeString 用于对cookieValue进行编码的字符串，如果为null则不进行编码
     */
    private static void doSetCookie(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String cookieName,
                                    String cookieValue,
                                    int cookieMaxAge,
                                    String encodeString) {
        try {
            // 如果cookie值为null，设置为空字符串
            if (cookieValue == null) {
                cookieValue = "";
            } else if (encodeString != null) { // 避免 NullPointerException
                // 对cookie值进行编码，防止特殊字符导致的问题
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }

            // 创建cookie对象
            Cookie cookie = new Cookie(cookieName, cookieValue);
            // 如果cookieMaxAge大于0，设置cookie的最大年龄
            if (cookieMaxAge > 0)
                cookie.setMaxAge(cookieMaxAge);

            // 如果请求对象不为空，尝试设置域名的cookie
            if (request != null) {
                // 获取请求的域名
                String domainName = getDomainNameForCookie(request);
                logger.info("========== domainName: {} ==========", domainName);
                // 如果域名不为空且不是localhost，设置cookie的域名
                if (!domainName.isEmpty() && !"localhost".equals(domainName)) {
                    String trimmedDomain = domainName.startsWith(".") ? domainName.substring(1) : domainName;
                    cookie.setDomain(trimmedDomain);
                }
            }

            // 设置cookie的路径为根路径，确保在整个网站内都能访问到这个cookie
            cookie.setPath("/");

            // 将cookie添加到响应对象中
            response.addCookie(cookie);

        } catch (UnsupportedEncodingException e) {
            // 捕获并记录对cookie值进行编码时的异常
            logger.error("Unsupported encoding when setting cookie", e);
        } catch (Exception e) {
            // 捕获并记录设置cookie时的其他异常
            logger.error("Error while setting cookie", e);
        }
    }



    /**
     * 根据HTTP请求获取适用于Cookie的域名。
     *
     * @param request HTTP请求对象，用于从中提取URL信息。
     * @return 返回处理后的域名字符串，格式化为适用于Cookie设置的形式。
     * @Description: 本方法从给定的HTTP请求中提取URL，并解析出适合用于设置Cookie的域名部分。
     * 处理逻辑考虑了多种域名格式，并能正确识别和处理常见的域名结构。
     */
    private static String getDomainNameForCookie(HttpServletRequest request) {
        // 初始化域名变量
        String domainName = null;

        // 获取请求的URL
        String serverName = request.getRequestURL().toString();

        // 如果URL为空，直接返回空字符串
        if (serverName.isEmpty()) {
            return "";
        }

        // 转换URL为小写并去除"http://"前缀
        serverName = serverName.toLowerCase().substring(7);

        // 提取域名部分
        final int end = serverName.indexOf("/");
        serverName = serverName.substring(0, end);

        // 移除可能存在的端口号
        if (serverName.contains(":")) {
            serverName = serverName.split(":")[0];
        }

        // 分割域名成数组
        final String[] domains = serverName.split("\\.");
        int len = domains.length;

        // 根据域名长度决定返回格式
        if (len > 3 && !isIp(serverName)) {
            // 三级域名格式
            domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
        } else if (len <= 3 && len > 1) {
            // 二级域名格式
            domainName = "." + domains[len - 2] + "." + domains[len - 1];
        } else {
            // 直接使用原始域名
            domainName = serverName;
        }

        // 返回处理后的域名
        return domainName;
    }


    private static final Pattern IP_PATTERN = Pattern.compile(
            "^" +                                                           // start of string
                    "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +  // three groups of numbers separated by dots
                    "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"             // last group of numbers
    );

    public static boolean isIp(String ip) {
        return IP_PATTERN.matcher(ip).matches();
    }

    /*******************************************************************
     *                     工具类测试
     *******************************************************************/

    public static void main(String[] args) {
//        testIsIP();
        testExtractDomainName();
    }

    private static void testExtractDomainName() {
        // 创建模拟的HttpServletRequest对象
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

        // 定义测试数据
        String testUrl1 = "http://www.example.com";
        String testUrl2 = "http://subdomain.example.co.uk";
        String testUrl3 = "http://192.168.1.1";

        // 模拟请求URL
        Mockito.when(mockRequest.getRequestURL()).thenReturn(new StringBuffer(testUrl1));
        System.out.println("Test URL 1: " + testUrl1 + " -> " + getDomainNameForCookie(mockRequest));

        Mockito.when(mockRequest.getRequestURL()).thenReturn(new StringBuffer(testUrl2));
        System.out.println("Test URL 2: " + testUrl2 + " -> " + getDomainNameForCookie(mockRequest));

        Mockito.when(mockRequest.getRequestURL()).thenReturn(new StringBuffer(testUrl3));
        System.out.println("Test URL 3: " + testUrl3 + " -> " + getDomainNameForCookie(mockRequest));
    }

    private static void testIsIP() {
        String[] testIPs = {"192.168.1.1", "255.255.255.255", "10.0.0.1", "172.16.0.1", "2001:0db8:85a3:0000:0000:8a2e:0370:7334", "invalidIP"};

        for (String ip : testIPs) {
            System.out.println(ip + " is a valid IPv4: " + isIp(ip));
        }
    }
}
