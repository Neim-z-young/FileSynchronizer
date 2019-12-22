package com.oyoungy.servlet;

import com.oyoungy.context.ApplicationContext;
import com.oyoungy.context.SingletonInstance;
import com.oyoungy.util.SyncFileInfoForm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class FileSourceSyncer extends HttpServlet {
    Logger logger = Logger.getLogger(FileSourceSyncer.class.getName());

    /**
     * 处理获取文件请求
     * @param request
     * @param response
     * @throws ServletException
     * @throws java.io.IOException
     */
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, java.io.IOException {

    }

    /**
     * 源文件服务器处理协调器的请求
     * @param request
     * @param response
     * @throws ServletException
     * @throws java.io.IOException
     */
    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, java.io.IOException {
        SyncFileInfoForm sFIF = null;
        try {
            sFIF = SyncFileInfoForm.parseHttpRequest(request);
        } catch (IllegalAccessException e) {
            logger.warning("wrong request body");
            e.printStackTrace();
        }
        if(sFIF!=null && sFIF.getTargetIp()!=null){
            ApplicationContext context = SingletonInstance.getAppContext();
            String targetKey = ApplicationContext.buildTargetServerKey(sFIF);
            context.getTargetServers().put(targetKey, "");
        }
    }

}
