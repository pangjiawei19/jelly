package com.pjw.iw.jelly;

/**
 * @author pangjiawei - [Created on 2019/5/29 20:21]
 */
public class JellyAssistant {

    public static boolean isValidLevel(int level) {
        return level >= JellyManager.LEVEL_MIN && level <= JellyManager.LEVEL_MAX;
    }
}
