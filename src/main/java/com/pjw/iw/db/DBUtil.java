package com.pjw.iw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author pangjiawei - [Created on 2019/5/29 16:34]
 */
public class DBUtil {

    private static String url;
    private static String user;
    private static String password;

    public static void main(String[] args) throws Exception {
        init();
    }

    public static void init() throws Exception {
        Properties properties = new Properties();
        properties.load(DBUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));

        String driver = properties.getProperty("driver");
        Class.forName(driver);

        url = properties.getProperty("url");
        user = properties.getProperty("user");
        password = properties.getProperty("password");


        for (String name : properties.stringPropertyNames()) {
            System.out.println(name + ":" + properties.getProperty(name));
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
