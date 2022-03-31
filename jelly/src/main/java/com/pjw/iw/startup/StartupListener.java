package com.pjw.iw.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.pjw.iw.db.DBUtil;
import com.pjw.iw.jelly.JellyManager;

/**
 * @author pangjiawei - [Created on 2019/5/29 15:19]
 */
public class StartupListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            //DB初始化
            DBUtil.init();

            //果冻模块初始化
            JellyManager.init();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //启动成功输出
        System.out.println("   ___      _ _         _____ _             _           _   _ \n" +
                "  |_  |    | | |       /  ___| |           | |         | | | |\n" +
                "    | | ___| | |_   _  \\ `--.| |_ __ _ _ __| |_ ___  __| | | |\n" +
                "    | |/ _ \\ | | | | |  `--. \\ __/ _` | '__| __/ _ \\/ _` | | |\n" +
                "/\\__/ /  __/ | | |_| | /\\__/ / || (_| | |  | ||  __/ (_| | |_|\n" +
                "\\____/ \\___|_|_|\\__, | \\____/ \\__\\__,_|_|   \\__\\___|\\__,_| (_)\n" +
                "                 __/ |                                        \n" +
                "                |___/                                         ");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        //关闭成功输出
        System.out.println("   ___      _ _         _____ _                       _   _ \n" +
                "  |_  |    | | |       /  ___| |                     | | | |\n" +
                "    | | ___| | |_   _  \\ `--.| |_ ___  _ __   ___  __| | | |\n" +
                "    | |/ _ \\ | | | | |  `--. \\ __/ _ \\| '_ \\ / _ \\/ _` | | |\n" +
                "/\\__/ /  __/ | | |_| | /\\__/ / || (_) | |_) |  __/ (_| | |_|\n" +
                "\\____/ \\___|_|_|\\__, | \\____/ \\__\\___/| .__/ \\___|\\__,_| (_)\n" +
                "                 __/ |                | |                   \n" +
                "                |___/                 |_|                   ");
    }
}
