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

    /**
     * 判断指定等级是否有效
     */
    public static boolean isValidLevel(int level) {
        return level >= JellyManager.LEVEL_MIN && level <= JellyManager.LEVEL_MAX;
    }

    /**
     * 判断参数指定的区域是否有效
     */
    public static boolean isValidExplodeArea(int row0, int col0, int row1, int col1) {
        if (row0 > row1 || col0 > col1) {
            return false;
        }

        if (row0 < 0
                || row1 >= JellyManager.JELLY_ARRAY_ROW_COUNT
                || col0 < 0
                || col1 > JellyManager.JELLY_ARRAY_COL_COUNT) {
            return false;
        }

        return true;
    }

    /**
     * 根据行列计算格子的数值
     */
    public static int calculatePointNum(int row, int col) {
        return row * POINT_NUM_ROW_COL_RATIO + col;
    }

    /**
     * 根据格子数值计算所在行
     */
    public static int calculateRowByPointNum(int pointNum) {
        return pointNum / POINT_NUM_ROW_COL_RATIO;
    }

    /**
     * 根据格子数值计算所在列
     */
    public static int calculateColByPointNum(int pointNum) {
        return pointNum % POINT_NUM_ROW_COL_RATIO;
    }

    /**
     * 查询所有等级对应初始布局信息
     */
    public static Map<Integer, char[][]> findAllInitialArray() {
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

    /**
     * 根据指定sessionId查询游戏布局信息，如果查询不到或发生异常返回null
     */
    public static char[][] findArray(String sessionId) {
        String sql = "select array from array where id = ?";

        synchronized (sessionId.intern()) {
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
        }

        return null;
    }

    /**
     * 创建一场游戏内容并存储，返回本场游戏的sessionId，如果创建失败则返回null
     */
    public static String saveArray(char[][] array) {

        String sql = "insert into array values(?,?)";

        String arrayText = ArrayUtil.array2String(array);
        String sessionId = null;

        try (Connection connection = DBUtil.getConnection()) {
            int count = 100;//这里循环100次，防止死循环
            while (count-- > 0) {
                //随机一个sessionId，对应数据库表里的主键，利用了表的主键唯一性，如果插入失败则再次循环重新生成sessionId，并存储
                sessionId = CommonUtil.randomKey(String.valueOf(System.currentTimeMillis()), 20);

                //对sessionId进行加锁
                synchronized (sessionId.intern()) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, sessionId);
                        preparedStatement.setString(2, arrayText);

                        preparedStatement.executeUpdate();
                        break;
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        sessionId = null;//存储失败将sessionId置空，等待下次重新分配，若循环次数用完，则最后会返回null
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessionId;
    }

    /**
     * 更新指定sessionId对应的游戏布局信息
     */
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

    public static boolean saveInitialArray(int level, char[][] array) {
        String sql = "insert into level values(?,?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, level);
            preparedStatement.setString(2, ArrayUtil.array2String(array));

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void printJellyArray(char[][] array) {
        for (char[] chars : array) {
            for (char c : chars) {
                System.out.print(c);
            }
            System.out.println();
        }
    }
}
