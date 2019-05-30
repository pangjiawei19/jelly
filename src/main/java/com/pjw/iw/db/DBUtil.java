package com.pjw.iw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DB工具类
 *
 * 1.通过JDBC使用MySQL数据库
 * 2.Demo为了简单起见，没有使用连接池，每次DB操作都会创建一个新连接，用完关闭，后续优化可加入连接池
 * 3.Demo为了简单起见，对于DB的多个操作没有使用事务，后续优化可以加入事务
 * 4.所有SQL执行都使用PreparedStatement，提高效率并防止sql注入
 *
 * @author pangjiawei - [Created on 2019/5/29 16:34]
 */
public class DBUtil {

    private static String url;
    private static String user;
    private static String password;

    /**
     * DB连接参数初始化
     */
    public static void init() throws Exception {
        Properties properties = new Properties();
        properties.load(DBUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));

        String driver = properties.getProperty("driver");
        Class.forName(driver);

        url = properties.getProperty("url");
        user = properties.getProperty("user");
        password = properties.getProperty("password");
    }

    /**
     * 获得一个数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
