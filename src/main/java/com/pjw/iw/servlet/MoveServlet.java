package com.pjw.iw.servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pjw.iw.jelly.JellyAssistant;
import com.pjw.iw.jelly.JellyBomb;
import com.pjw.iw.util.ServletUtil;

/**
 * @author pangjiawei - [Created on 2019/5/29 20:46]
 */
@WebServlet(name = "MoveServlet", urlPatterns = "/move")
public class MoveServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        boolean success = false;

        try {

            String sessionId = request.getParameter("sessionId");
            int row0 = Integer.parseInt(request.getParameter("row0"));
            int col0 = Integer.parseInt(request.getParameter("col0"));
            int row1 = Integer.parseInt(request.getParameter("row1"));
            int col1 = Integer.parseInt(request.getParameter("col1"));

            if (JellyAssistant.isValidExplodeArea(row0, col0, row1, col1)
                    && sessionId != null && !sessionId.isEmpty()) {

                synchronized (sessionId.intern()) {
                    char[][] array = JellyAssistant.findArray(sessionId);
                    if (array != null) {
                        JellyBomb.explode(row0, col0, row1, col1, array);
                        JellyAssistant.updateArray(sessionId, array);
                        ServletUtil.printCharArray(array, response.getWriter());
                        success = true;
                    }
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
