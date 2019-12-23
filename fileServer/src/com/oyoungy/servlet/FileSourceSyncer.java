package com.oyoungy.servlet;

import com.oyoungy.context.ApplicationContext;
import com.oyoungy.context.SingletonInstance;
import com.oyoungy.util.SyncFileInfoForm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String fullPath;
        if((fullPath = validatePeerServer(request)).equals("")){
            response.sendError(404, "请求无效");
        }
        processFileSync(request, response, fullPath);
    }

    private String validatePeerServer(HttpServletRequest request){
        String fullPath = "";
        try {
            BufferedReader reader = request.getReader();
            fullPath = reader.readLine();
            SyncFileInfoForm sFIF = new SyncFileInfoForm();
            sFIF.setTargetIp(request.getRemoteAddr());
            sFIF.setTargetPort(String.valueOf(request.getRemotePort()));
            sFIF.setSourceFile(fullPath);
            String key = ApplicationContext.buildTargetServerKey(sFIF);
            if(!SingletonInstance.getAppContext().getTargetServers().containsKey(key)){
                fullPath = "";
            }
        } catch (IOException e) {
            logger.warning("IO Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return fullPath;
    }

    /**
     * 一次性发送所有数据
     * @param request
     * @param response
     * @param fullPath
     */
    private void processFileSync(HttpServletRequest request, HttpServletResponse response, String fullPath){
        Path path = Paths.get(fullPath);
        String errmsg = "";
        boolean wrong = false;
        if(!path.isAbsolute()){
            errmsg += "文件不是绝对路径;";
            wrong = true;
        }
        if(Files.notExists(path)){
            errmsg += "源文件不存在;";
            wrong = true;
        }
        if(!Files.isReadable(path)){
            errmsg += "文件不可读;";
            wrong = true;
        }
        if(Files.isDirectory(path)){
            errmsg +="源路径只是目录;";
            wrong = true;
        }
        if(wrong){
            logger.warning(errmsg);
            try {
                response.sendError(404, errmsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        int BUFFER_SIZE = 4096;
        InputStream in = null;
        OutputStream out = null;

        logger.info("Begin to push file");

        try{
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream");
            response.setContentLength((int) Files.size(path));   //TODO 文件长度有限,分块传输
            response.setHeader("Accept-Ranges", "bytes");

            int readLength = 0;

            in = new BufferedInputStream(Files.newInputStream(path), BUFFER_SIZE);
            out = new BufferedOutputStream(response.getOutputStream());

            byte[] buffer = new byte[BUFFER_SIZE];
            while ((readLength=in.read(buffer)) > 0) {
                byte[] bytes = new byte[readLength];
                System.arraycopy(buffer, 0, bytes, 0, readLength);
                out.write(bytes);
            }

            out.flush();

            response.addHeader("key", fullPath);
            response.setStatus(200);
            logger.info("source file sync success");
        }catch(Exception e){
            e.printStackTrace();
            logger.warning("exception occurred: " + e.getMessage());
            response.setStatus(404);
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.warning("Stream close exception");
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.warning("Stream close exception");
                }
            }
        }
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
