package xyz.nyc1.backend;

public class CompareMD5 {
    // 校验MD5 是否相同
    public static boolean compare(String md5, String md5_2) {
        // 用md5.equals() 方法比较两个字符串是否相同
        return md5.equals(md5_2);
    }
}