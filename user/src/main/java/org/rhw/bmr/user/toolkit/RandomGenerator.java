package org.rhw.bmr.user.toolkit;


import java.security.SecureRandom;

/**
 * 分组ID随机生成
 */
public final class RandomGenerator {
    private final static String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final static SecureRandom random = new SecureRandom();

    /**
     * 生成随机分组ID
     * @param length ID长度
     * @return  ID
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }


    public static String generateRandomString() {
        return generateRandomString(6);
    }

}
