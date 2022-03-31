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
     * 等级对应初始布局，key：level，value：初始布局二维数组
     */
    private static Map<Integer, char[][]> level2InitialArray = new HashMap<>();

    private JellyManager() {

    }

    public static JellyManager getInstance() {
        return instance;
    }

    /**
     * 初始化，加载初始布局，如果不存在随机创建并保存
     */
    public static void init() {
        //加载初始布局
        loadInitialArray();

        //遍历各个等级，如果未加载则创建并存储
        for (int level = JellyManager.LEVEL_MIN; level <= JellyManager.LEVEL_MAX; level++) {
            if (!level2InitialArray.containsKey(level)) {
                //随机一组布局
                char[][] array = JellyBomb.randomJellyArray();

                //存储初始布局
                if (JellyAssistant.saveInitialArray(level, array)) {
                    level2InitialArray.put(level, array);
                } else {
                    throw new RuntimeException("save initial array error, level:" + level);
                }
            }
        }
    }

    /**
     * 加载初始布局
     */
    public static void loadInitialArray() {
        level2InitialArray.clear();
        level2InitialArray.putAll(JellyAssistant.findAllInitialArray());
    }

    /**
     * 获得指定等级对应的初始布局（副本）
     */
    public static char[][] getInitialArrayByLevel(int level) {
        char[][] array = level2InitialArray.get(level);
        char[][] result = new char[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return result;
    }
}
