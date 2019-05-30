package com.pjw.iw.jelly;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.pjw.iw.db.DBUtil;
import com.pjw.iw.util.ArrayUtil;
import com.pjw.iw.util.CommonUtil;

/**
 * @author pangjiawei - [Created on 2019/5/29 20:21]
 */
public class JellyAssistant {

    public static final int POINT_NUM_ROW_COL_RATIO = 1000;

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

    public static int calculatePointNum(int row, int col) {
        return row * POINT_NUM_ROW_COL_RATIO + col;
    }

    public static int calculateRowByPointNum(int pointNum) {
        return pointNum / POINT_NUM_ROW_COL_RATIO;
    }

    public static int calculateColByPointNum(int pointNum) {
        return pointNum % POINT_NUM_ROW_COL_RATIO;
    }

    public static Map<Integer, char[][]> findAllLevelArray() {
        Map<Integer, char[][]> levelArray = new HashMap<>();

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

        return levelArray;
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

                        preparedStatement.executeUpdate();
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

    public static void updateArray(String sessionId, char[][] array) {

        String arrayText = ArrayUtil.array2String(array);
        String sql = "update array set array = ? where id = ?";

        synchronized (sessionId.intern()) {
            try (Connection connection = DBUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, arrayText);
                preparedStatement.setString(2, sessionId);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<Integer, char[][]> generateInitialArray() {
        Map<Integer, char[][]> map = new HashMap<>();

        String sql = "insert into level values(?,?)";
        for (int level = JellyManager.LEVEL_MIN; level <= JellyManager.LEVEL_MAX; level++) {
            char[][] array = JellyBomb.randomJellyArray();

            try (Connection connection = DBUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, level);
                preparedStatement.setString(2, ArrayUtil.array2String(array));

                preparedStatement.executeUpdate();

                map.put(level, array);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    public static void printBombArray(char[][] array) {
        for (char[] chars : array) {
            for (char c : chars) {
                System.out.print(c);
            }
            System.out.println();
        }
    }
}
