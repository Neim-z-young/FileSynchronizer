package com.oyoungy.Servlet;

import com.oyoungy.util.FileSyncForm;
import com.oyoungy.util.SingletonInstance;
import com.oyoungy.util.TimerConfig;
import com.sun.net.httpserver.HttpServer;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Logger;

public class Coordinator extends HttpServlet {
    Logger logger = Logger.getLogger(Coordinator.class.getName());

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, java.io.IOException {

    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, java.io.IOException {

        FileSyncForm fSF = null;
        try {
            fSF = FileSyncForm.parseHttpRequest(request);
        } catch (IllegalAccessException e) {
            fSF = new FileSyncForm();
            e.printStackTrace();
            logger.warning("Exception: " + e.getMessage());
        }

        TimerConfig timerConfig = SingletonInstance.getTimerContext();
        String key = timerConfig.buildKey(fSF.toTaskParam());

        TimerTask task = timerConfig.buildTaskAndPushToSchedule(fSF);
        timerConfig.addTimerConfig(key, task);
        response.setContentType("text/html;charset=UTF-8");
        //   response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");   //设置为与服务器同编码（告诉服务器怎样解码）
        String user=request.getParameter("username");
        java.io.PrintWriter out= response.getWriter();
        // output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>File Sync Servlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("Sync task is set: "+key);
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
