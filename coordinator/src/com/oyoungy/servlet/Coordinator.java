package com.oyoungy.servlet;

import com.oyoungy.util.FileSyncForm;
import com.oyoungy.util.SingletonInstance;
import com.oyoungy.util.TimerConfig;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.TimerTask;
import java.util.logging.Logger;

public class Coordinator extends HttpServlet {
    Logger logger = Logger.getLogger(Coordinator.class.getName());

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, java.io.IOException {

    }

    /**
     * 解析并发送文件同步请求
     * @param request
     * @param response
     * @throws ServletException
     * @throws java.io.IOException
     */
    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, java.io.IOException {

        FileSyncForm fSF = null;
        //TODO 验证文件路径的合法性
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
        if(task!=null){
            timerConfig.addTimerConfig(key, task);
        }

        response.setContentType("text/html;charset=UTF-8");
        //   response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");   //设置为与服务器同编码（告诉服务器怎样解码）
        java.io.PrintWriter out= response.getWriter();
        // output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>File Sync Servlet</title>");
        out.println("</head>");
        out.println("<body><br/>");
        out.println("Sync task is: "+task+"<br/>");
        out.println("Sync key is: "+key);
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
