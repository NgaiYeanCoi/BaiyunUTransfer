package xyz.nyc1.backend;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Example {
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : messageDigest) {
                int num = b & 0xff;
                String str = Integer.toHexString(num);
                if (str.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(str);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
