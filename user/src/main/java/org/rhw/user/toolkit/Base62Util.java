package org.rhw.user.toolkit;

public class Base62Util {
    private static final char[] CHARS = "vrITQJ70L6CgRmWB2ipN9M5xSGPlKsuwfhDAdZYynFcXobVEe4U3aj1O8tkzHq".toCharArray();

    /**
     * 将十进制数转换为 Base62 编码
     * @param num 要转换的长整型数字
     * @return Base62 编码后的字符串
     */
    public static String convertDecToBase62(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int index = (int) (num % 62); // 对62取余得到字符索引
            sb.append(CHARS[index]);
            num /= 62; // 除以62
        }
        return sb.reverse().toString(); // 反转结果返回
    }
}

