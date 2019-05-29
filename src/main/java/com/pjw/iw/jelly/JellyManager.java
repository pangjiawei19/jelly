package com.pjw.iw.jelly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.pjw.iw.db.DBUtil;
import com.pjw.iw.util.ArrayUtil;
import com.pjw.iw.util.CommonUtil;

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
            generateInitialArray();
            loadInitialArray();
        }
    }

    public static void loadInitialArray() {
        levelArray.clear();

        String sql = "select level,array from level";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int level = rs.getInt("level");
                String array = rs.getString("array");

                levelArray.put(level, ArrayUtil.string2Array(array));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static char[][] getArrayByLevel(int level) {
        char[][] array = levelArray.get(level);
        char[][] result = new char[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return result;
    }

    public static char[][] findArray(String sessionId) {
        String sql = "select array from array where id = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, sessionId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String array = rs.getString("array");
                    return ArrayUtil.string2Array(array);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveArray(char[][] array) {

        String sql = "insert into array values(?,?)";

        String arrayText = ArrayUtil.array2String(array);
        String sessionId = null;

        try (Connection connection = DBUtil.getConnection()) {
            int count = 100;
            while (count-- > 0) {
                sessionId = CommonUtil.randomKey(String.valueOf(System.currentTimeMillis()), 20);
                synchronized (sessionId.intern()) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, sessionId);
                        preparedStatement.setString(2, arrayText);

                        preparedStatement.execute();
                        break;
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessionId;
    }

    public static void updateArray(String id, char[][] array) {

        String arrayText = ArrayUtil.array2String(array);
        String sql = "update array set array = ? where id = ?";

        synchronized (id.intern()) {
            try (Connection connection = DBUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, arrayText);
                preparedStatement.setString(2, id);

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void generateInitialArray() {
        String sql = "insert into level values(?,?)";
        for (int i = LEVEL_MIN; i <= LEVEL_MAX; i++) {
            int level = i;
            char[][] array = ArrayUtil.generateArray();

            try (Connection connection = DBUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, level);
                preparedStatement.setString(2, ArrayUtil.array2String(array));

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
