package com.oyoungy.util;

import com.oyoungy.context.SingletonInstance;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Logger;

public class HttpFileSyncer implements SyncFileService {
    Logger logger = Logger.getLogger(HttpFileSyncer.class.getName());

    private boolean retry;
    private int count;

    public HttpFileSyncer(){
        retry = true;
        count = 3;
    }

    @Override
    public boolean syncFileByParam(SyncFileInfoForm form) {
        HttpClient client = SingletonInstance.getAppContext().getHttpClient();
        HttpResponse response = null;

        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .header("Content-Type", "text/html")
                    .version(HttpClient.Version.HTTP_2)
                    .uri(URI.create("http://"+form.getSourceIp()+":"+form.getSourcePort()+"/fileServer/FileSourceSync"))
                    .timeout(Duration.ofMillis(5000))
                    .method("GET", HttpRequest.BodyPublishers.ofString(form.getSourceFile()))
                    .build();

            //发送请求，失败的话就休眠3秒重试最多count次（避免协调器发送同步请求时可能造成的源文件服务器的延迟）
            //TODO BodyHandler
            while (retry){
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if(response.statusCode()==200 || --count<0){
                    retry=false;
                }else {
                    Thread.sleep(3000);
                }
            }
            if(response.statusCode()==200){
                //TODO 保存文件
                return true;
            }
        } catch (IOException e) {
            logger.warning("IO exception!!!");
            logger.warning(e.getMessage());
        } catch (InterruptedException e) {
            logger.warning("Interrupted exception!!!");
            logger.warning(e.getMessage());
        }catch (Exception e){
            logger.warning("Exception!!!");
            logger.warning(e.getMessage());
        }

        return false;
    }


}
