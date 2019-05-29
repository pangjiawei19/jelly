package com.pjw.iw.util;

import java.util.Arrays;
import java.util.Random;

import com.pjw.iw.db.DBUtil;
import com.pjw.iw.jelly.JellyManager;

/**
 * @author pangjiawei - [Created on 2019/5/29 17:24]
 */
public class ArrayUtil {

    public static String array2String(char[][] array) {
        if (array == null || array.length < 1) {
            return "null";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(array.length).append(",");
        for (char[] chars : array) {
            builder.append(chars.length).append(",");
            for (char aChar : chars) {
                builder.append(aChar).append(",");
            }
        }

        return builder.substring(0, builder.length() - 1);
    }

    public static char[][] string2Array(String text) {
        if (text == null || text.isEmpty() || text.equals("null")) {
            return null;
        }

        int index = 0;
        String[] strArray = text.split(",");
        int rowCount = Integer.parseInt(strArray[index++]);

        char[][] array = new char[rowCount][];
        for (int i = 0; i < rowCount; i++) {
            int colCount = Integer.parseInt(strArray[index++]);
            array[i] = new char[colCount];
            for (int j = 0; j < colCount; j++) {
                String str = strArray[index++];
                array[i][j] = str.charAt(0);
            }
        }

        return array;
    }

    public static void main(String[] args) throws Exception {

        char[][] c = generateArray();
        System.out.println(Arrays.deepToString(c));
//
//        String text = array2String(c);
//        System.out.println(text);
//
//        System.out.println(Arrays.deepToString(string2Array(text)));

        DBUtil.init();
        String id = JellyManager.saveArray(c);

        JellyManager.updateArray(id, generateArray());

    }

    public static char[][] generateArray() {
        char[][] c = new char[8][8];

        Random random = new Random();
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[i].length; j++) {
                c[i][j] = (char) ('A' + random.nextInt(10));
            }
        }
        return c;
    }
}
