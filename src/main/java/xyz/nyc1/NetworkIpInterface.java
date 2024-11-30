package xyz.nyc1;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;


/**
 * @author NgaiYeanCoi
 */

public class NetworkIpInterface {
    /**
     * 获取主机的所有IP地址
     * @return IP地址（String）
     */

    public static String getHostIPs() {
        StringBuilder ipAddresses = new StringBuilder();
        try {
            // 获取所有网络接口
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            // 遍历所有网络接口
            for (NetworkInterface networkInterface : Collections.list(interfaces)) {
                // 获取该网络接口的所有IP地址
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    // 过滤掉回环地址和非IPv4地址
                    if (!inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().indexOf(':') == -1) {
                        ipAddresses.append(inetAddress.getHostAddress()).append("\n");
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return ipAddresses.toString();
    }

}