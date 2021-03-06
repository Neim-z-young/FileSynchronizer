package com.oyoungy.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * 同步器定时发送文件同步请求
 */
public class FileSyncTimerTask extends TimerTask {
    private Logger logger = Logger.getLogger(FileSyncTimerTask.class.getName());

    private String sourceIp;
    private String sourcePort;
    private String sourceFile;
    private String targetIp;
    private String targetPort;
    private String targetFile;

    private String syncType;
    private String period;

    public FileSyncTimerTask(FileSyncTaskParam param){
        this(param.getSourceIp(), param.getSourcePort(), param.getSourceFile(),
                param.getTargetIp(), param.getTargetPort(), param.getTargetFile());
    }

    public FileSyncTimerTask(String sourceIp, String sourcePort, String sourceFile,
                             String targetIp, String targetPort, String targetFile){
        this.sourceIp = sourceIp;
        this.sourcePort = sourcePort;
        this.sourceFile = sourceFile;
        this.targetIp = targetIp;
        this.targetPort = targetPort;
        this.targetFile = targetFile;
    }

    protected boolean preValidate(){
        return true;
    }

    protected String buildTargetData(){
        StringBuilder sb = new StringBuilder();
        //TODO 修正硬编码
        String sep = ": ", sep2 = ", ";
        sb.append("sourceIp").append(sep).append(sourceIp);
        sb.append(sep2).append("sourcePort").append(sep).append(sourcePort);
        sb.append(sep2).append("sourceFile").append(sep).append(sourceFile);
        sb.append(sep2).append("targetFile").append(sep).append(targetFile);
        sb.append(sep2);
        return sb.toString();
    }

    protected String buildSourceData(){
        StringBuilder sb = new StringBuilder();
        //TODO 修正硬编码
        String sep = ": ", sep2 = ", ";
        sb.append("targetIp").append(sep).append(targetIp);
        sb.append(sep2).append("targetPort").append(sep).append(targetPort);
        sb.append(sep2).append("sourceFile").append(sep).append(sourceFile);
        sb.append(sep2);
        return sb.toString();
    }

    @Override
    public void run() {
        if(!preValidate()){
            return;
        }
        HttpClient client = SingletonInstance.getHttpClientContext();

        String targetData = buildTargetData();
        String sourceData = buildSourceData();

        try {
            //TODO 两个请求到达对应服务器的时间不确定，故目标服务器可能先收到请求，这可能会造成后续的认证出错，故后续出错可从这里入手
            //文件源服务器请求
            logger.info("request to source server: " + sourceIp + " : " + sourceData);
            HttpRequest sourceRequest = HttpRequest
                    .newBuilder()
                    .header("Content-Type", "text/plain")
                    .version(HttpClient.Version.HTTP_2)
                    .uri(URI.create("http://"+ sourceIp +":"+sourcePort+"/fileServer/FileSourceSync"))
                    .timeout(Duration.ofMillis(5000))
                    .POST(HttpRequest.BodyPublishers.ofString(sourceData))
                    .build();
            CompletableFuture<HttpResponse<String>> cf2 = client.sendAsync(sourceRequest, HttpResponse.BodyHandlers.ofString());

            //文件目标服务器请求
            logger.info("request to target server: " + targetIp + " : " + targetData);
            HttpRequest targetRequest = HttpRequest
                    .newBuilder()
                    .header("Content-Type", "text/plain")
                    .version(HttpClient.Version.HTTP_2)
                    .uri(URI.create("http://"+ targetIp +":"+targetPort+"/fileServer/FileTargetSync"))
                    .timeout(Duration.ofMillis(5000))
                    .POST(HttpRequest.BodyPublishers.ofString(targetData))
                    .build();
            CompletableFuture<HttpResponse<String>> cf = client.sendAsync(targetRequest, HttpResponse.BodyHandlers.ofString());

        }catch (IllegalArgumentException e){
            logger.warning("IllegalArgument exception occurred :" + e.getMessage());
            logger.warning("canceling task.....");
            this.cancel();
        }catch (Exception e){
            logger.warning("Other exception occurred :" + e.getMessage());
            logger.warning("canceling task.....");
            this.cancel();
        }
    }
}
