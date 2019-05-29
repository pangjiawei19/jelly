package com.pjw.iw.util;

import java.util.Random;

/**
 * @author pangjiawei - [Created on 2019/5/29 18:28]
 */
public class CommonUtil {

    private static Random random = new Random();

    private static char[] keyDictionary;

    static {
        char[] chars = new char[62];
        for (int i = 0; i < 10; i++) {
            chars[i] = (char) ('0' + i);
        }
        for (int i = 0; i < 26; i++) {
            chars[i + 10] = (char) ('a' + i);
        }
        for (int i = 0; i < 26; i++) {
            chars[i + 36] = (char) ('A' + i);
        }
        keyDictionary = chars;
    }

    public static String randomKey(String prefix, int num) {
        if (prefix.length() >= num) {
            return prefix;
        }

        StringBuilder sb = new StringBuilder(prefix);

        while (sb.length() < num) {
            sb.append(keyDictionary[random.nextInt(keyDictionary.length)]);
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(randomKey(String.valueOf(System.currentTimeMillis()), 20));
    }

}
