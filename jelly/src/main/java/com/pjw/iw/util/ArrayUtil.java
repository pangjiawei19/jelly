package com.pjw.iw.util;

/**
 * 数组工具类
 *
 * @author pangjiawei - [Created on 2019/5/29 17:24]
 */
public class ArrayUtil {

    /**
     * 二维数组转字符串，如果数组为null或空，返回字符串"null"
     */
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

    /**
     * 字符串转二维数组，如果文本不符合格式则返回null
     *
     * @param text 必须是{@link ArrayUtil#array2String(char[][])}返回的字符串
     */
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
}
