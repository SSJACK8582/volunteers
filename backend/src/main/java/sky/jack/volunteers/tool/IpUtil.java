package sky.jack.volunteers.tool;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {
    private static final String[] IP_HEADERS = {
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };
    private static final String IP_UNKNOWN = "unknown";
    private static final String IP_LOCAL_IPV4 = "127.0.0.1";
    private static final String IP_LOCAL_IPV6 = "0:0:0:0:0:0:0:1";

    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        for (String header : IP_HEADERS) {
            ip = request.getHeader(header);
            if (!isEmptyIP(ip)) {
                break;
            }
        }
        if (isEmptyIP(ip)) {
            ip = request.getRemoteAddr();
            if (isLocalIP(ip)) {
                try {
                    ip = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return isEmptyIP(ip) ? request.getRemoteAddr() : ip;
    }

    private static boolean isEmptyIP(String ip) {
        return ip == null || ip.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ip);
    }

    private static boolean isLocalIP(String ip) {
        return IP_LOCAL_IPV4.equals(ip) || IP_LOCAL_IPV6.equals(ip);
    }
}
