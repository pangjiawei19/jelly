package com.pjw.iw.jelly;

/**
 * @author pangjiawei - [Created on 2019/5/29 20:21]
 */
public class JellyAssistant {

    public static boolean isValidLevel(int level) {
        return level >= JellyManager.LEVEL_MIN && level <= JellyManager.LEVEL_MAX;
    }

    public static boolean isValidExplodeArea(int row0, int col0, int row1, int col1) {
        if (row0 > row1 || col0 > col1) {
            return false;
        }

        if (row0 < 0 || row1 > 7 || col0 < 0 || col1 > 7) {
            return false;
        }

        return true;
    }
}
