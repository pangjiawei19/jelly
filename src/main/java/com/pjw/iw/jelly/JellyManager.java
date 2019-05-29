package com.pjw.iw.jelly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.pjw.iw.db.DBUtil;

/**
 * 优化点：
 * 1.PreparedStatement赋值封装方法
 * 2.没有使用事物
 *
 * @author pangjiawei - [Created on 2019/5/29 18:01]
 */
public class JellyManager {

    public static final int LEVEL_MIN = 1;
    public static final int LEVEL_MAX = 10;

    public static final int JELLY_ARRAY_ROW_COUNT = 8;
    public static final int JELLY_ARRAY_COL_COUNT = 8;


    private static final JellyManager instance = new JellyManager();

    private static Map<Integer, char[][]> levelArray = new HashMap<>();

    private JellyManager() {

    }

    public static JellyManager getInstance() {
        return instance;
    }

    public static void init() {
        loadInitialArray();

        if (levelArray.isEmpty()) {
            JellyAssistant.generateInitialArray();
            loadInitialArray();
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


    public static void main(String[] args) throws Exception {
        DBUtil.init();
        JellyManager.init();

        char[][] c = JellyManager.levelArray.get(1);
        System.out.println(Arrays.deepToString(c));
        char[][] d = getArrayByLevel(1);
        System.out.println(Arrays.deepToString(d));
        System.out.println(c == d);

//        JellyManager.generateInitialArray();
//        JellyManager.loadInitialArray();
//        for (char[][] chars : levelArray.values()) {
//            System.out.println(Arrays.deepToString(chars));
//        }
    }
}
