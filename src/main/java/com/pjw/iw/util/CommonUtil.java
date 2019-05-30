package com.pjw.iw.util;

import java.util.Random;

/**
 * 通用工具类
 *
 * @author pangjiawei - [Created on 2019/5/29 18:28]
 */
public class CommonUtil {

    private static Random random = new Random();

    /**
     * 关键字字典，包括所有数组和大小写字母
     */
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

    /**
     * 随机一个字符串key，如果指定长度小于等于前缀长度，直接返回前缀，否则从字典中随机字符拼接至前缀尾部，直到字符串长度满足指定长度
     *
     * @param prefix 前缀
     * @param num    key长度
     */
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
}
