package com.pjw.iw.util;

import java.io.PrintWriter;

/**
 * Servlet工具类
 *
 * @author pangjiawei - [Created on 2019/5/29 20:20]
 */
public class ServletUtil {

    /**
     * 参数异常时返回文本常量
     */
    public static final String INVALID_PARAM_TEXT = "INVALID PARAMS";

    /**
     * 按指定格式向writer中写入二维数组array
     */
    public static void printCharArray(char[][] array, PrintWriter writer) {
        for (char[] chars : array) {
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (i == chars.length - 1) {
                    writer.println(c);
                } else {
                    writer.print(c);
                }
            }
        }
    }
}
