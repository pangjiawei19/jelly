package com.pjw.iw.jelly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 果冻模块管理器
 *
 * @author pangjiawei - [Created on 2019/5/29 18:01]
 */
public class JellyManager {

    public static final int LEVEL_MIN = 1;
    public static final int LEVEL_MAX = 10;

    public static final int JELLY_ARRAY_ROW_COUNT = 8;
    public static final int JELLY_ARRAY_COL_COUNT = 8;

    private static final JellyManager instance = new JellyManager();

    /**
     * 等级对应初始布局，key：level，value：布局二维数组
     */
    private static Map<Integer, char[][]> levelArray = new HashMap<>();

    private JellyManager() {

    }

    public static JellyManager getInstance() {
        return instance;
    }

    /**
     * 初始化，加载初始布局，如果不存在随机创建并保存
     */
    public static void init() {
        loadInitialArray();

        if (levelArray.isEmpty()) {
            levelArray.putAll(JellyAssistant.generateInitialArray());
        }
    }

    public static void loadInitialArray() {
        levelArray.clear();
        levelArray.putAll(JellyAssistant.findAllLevelArray());
    }

    public static char[][] getArrayByLevel(int level) {
        char[][] array = levelArray.get(level);
        char[][] result = new char[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return result;
    }
}
