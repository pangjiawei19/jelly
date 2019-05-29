package com.pjw.iw.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pjw.iw.jelly.JellyAssistant;
import com.pjw.iw.jelly.JellyManager;
import com.pjw.iw.util.ServletUtil;

/**
 * @author pangjiawei - [Created on 2019/5/29 20:17]
 */
@WebServlet(name = "StartLevelServlet", urlPatterns = "/start-level")
public class StartLevelServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());


        boolean success = false;

        try {
            int level = Integer.parseInt(request.getParameter("level"));

            if (JellyAssistant.isValidLevel(level)) {
                char[][] array = JellyManager.getArrayByLevel(level);
                String id = JellyManager.saveArray(array);

                if (id != null) {
                    PrintWriter writer = response.getWriter();
                    writer.println(id);
                    ServletUtil.printCharArray(array, writer);

                    success = true;
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (!success) {
            response.getWriter().write(ServletUtil.INVALID_PARAM_TEXT);
        }
    }
}
