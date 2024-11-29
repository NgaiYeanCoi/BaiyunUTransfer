package xyz.nyc1.backend;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Example {
    // 调用直接 MD5Example.getMD5("String"); 即可;
    public static String getMD5(String input) {
        try {
            // 获取信息摘要对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuffer stringBuffer = new StringBuffer(); // 创建一个动态的StringBuffer 用于储存MD5
            // 把每个 byte做一个与运算 & 0xff 再转换成十进制
            for (byte b : messageDigest) {
                int num = b & 0xff;
                String str = Integer.toHexString(num);
                if (str.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(str);
            }
            // 返回一个 String 结果
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            // 抛出异常
            throw new RuntimeException(e);
        }
    }
}
