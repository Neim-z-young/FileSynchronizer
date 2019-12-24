package com.oyoungy.util;

import com.oyoungy.context.SingletonInstance;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.time.Duration;
import java.util.logging.Logger;

public class HttpFileSyncer implements SyncFileService {
    Logger logger = Logger.getLogger(HttpFileSyncer.class.getName());

    /**
     * 同步阻塞方法
     * @param form
     * @return
     */
    @Override
    public boolean syncFileByParam(SyncFileInfoForm form) {
        HttpClient client = SingletonInstance.getAppContext().getHttpClient();
        HttpResponse response = null;
        Path path = Paths.get(form.getTargetFile());
        if(!path.isAbsolute() || Files.isDirectory(path)){
            logger.warning("目标文件： " + form.getTargetFile() + " ;  target file is invalid");
            return false;
        }

        int BUFFER_SIZE = 4096;
        InputStream in = null;
        OutputStream out = null;
        logger.info("begin to pull file");
        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .header("Content-Type", "text/plain")
                    .version(HttpClient.Version.HTTP_2)
                    .uri(URI.create("http://"+form.getSourceIp()+":"+form.getSourcePort()+"/fileServer/FileSourceSync"))
                    .timeout(Duration.ofMillis(5000))
                    .method("GET", HttpRequest.BodyPublishers.ofString(form.getTargetPort() + "\n"+ form.getSourceFile()))
                    .build();

            boolean retry = true;
            int count = 3;
            //发送请求，失败的话就休眠3秒重试最多count次（避免协调器发送同步请求时可能造成的源文件服务器的延迟）
            //TODO BodyHandler
            while (retry){
                response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
                if(response.statusCode()==200 || --count<0){
                    retry=false;
                }else {
                    Thread.sleep(3000);
                }
                logger.info("retry file sync...");
            }
            if(response!=null && response.statusCode()==200){
                //TODO 保存文件
                //通过临时文件的方式同步
                Path temp = Files.createTempFile(null, null);
                logger.info("临时文件：" + temp.toString());
                in = new BufferedInputStream((InputStream) response.body(), BUFFER_SIZE);
                out = new BufferedOutputStream(Files.newOutputStream(temp,
                                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
                byte[] buffer = new byte[BUFFER_SIZE];
                int readLength = 0;
                while ((readLength = in.read(buffer)) > 0){
                    out.write(buffer, 0, readLength);
                }

                out.flush();
                out.close();
                out = null;

                Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
                logger.info("target file sync success");
                return true;
            }
        } catch (IOException e) {
            logger.warning("IO exception!!! " + e.getMessage());
        } catch (InterruptedException e) {
            logger.warning("Interrupted exception!!!" + e.getMessage());
            logger.warning(e.getMessage());
        }catch (Exception e){
            logger.warning("Exception!!!" + e.getMessage());
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

        logger.warning("target file sync failed");
        return false;
    }


}
