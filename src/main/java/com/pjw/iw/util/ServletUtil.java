package com.pjw.iw.util;

import java.io.PrintWriter;

/**
 * @author pangjiawei - [Created on 2019/5/29 20:20]
 */
public class ServletUtil {

    public static final String INVALID_PARAM_TEXT = "INVALID PARAMS";

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
