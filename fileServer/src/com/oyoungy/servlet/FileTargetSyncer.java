package com.oyoungy.servlet;

import com.oyoungy.context.ApplicationContext;
import com.oyoungy.context.SingletonInstance;
import com.oyoungy.util.FileSyncTask;
import com.oyoungy.util.SyncFileInfoForm;
import com.oyoungy.util.SyncFileService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class FileTargetSyncer extends HttpServlet {
    Logger logger = Logger.getLogger(FileTargetSyncer.class.getName());

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, java.io.IOException {

    }

    /**
     * 目标文件服务器处理协调器的请求
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
        if(sFIF!=null && sFIF.getSourceIp()!=null){
            ApplicationContext context = SingletonInstance.getAppContext();
            String sourceKey = ApplicationContext.buildSourceServerKey(sFIF);
            context.getSourceServers().put(sourceKey, "");
            //TODO 启动同步线程，在HTTP协议中用get请求同步文件，同步协议可自定义
            ThreadPoolExecutor poolExecutor = SingletonInstance.getPoolExecutor();
            SyncFileService service = SingletonInstance.getSyncService();
            FileSyncTask task = new FileSyncTask(service, sFIF);
            poolExecutor.execute(task);
        }
    }
}
