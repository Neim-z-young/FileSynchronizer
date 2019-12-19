package com.oyoungy.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

/**
 * 同步器定时发送文件同步请求
 */
public class FileSyncTimerTask extends TimerTask {
    private String sourceHost;
    private String sourcePort;
    private String sourceFile;
    private String targetHost;
    private String targetPort;
    private String targetDirectory;

    private String syncType;
    private String period;

    public FileSyncTimerTask(FileSyncTaskParam param){
        this(param.getSourceIp(), param.getSourcePort(), param.getSourceFile(),
                param.getTargetIp(), param.getTargetPort(), param.getTargetDirectory());
    }

    public FileSyncTimerTask(String sourceHost, String sourcePort, String sourceFile,
                             String targetHost, String targetPort, String targetDirectory){
        this.sourceHost = sourceHost;
        this.sourcePort = sourcePort;
        this.sourceFile = sourceFile;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.targetDirectory = targetDirectory;
    }

    protected boolean preValidate(){
        return true;
    }

    @Override
    public void run() {
        if(!preValidate()){
            return;
        }
        HttpClient client = SingletonInstance.getHttpClientContext();

        StringBuilder sb = new StringBuilder();
        sb.append("sourceIp: ").append(sourceHost);
        sb.append(", sourcePort: ").append(sourcePort);
        sb.append(", sourceFile: ").append(sourceFile);
        sb.append(", targetDirectory").append(targetDirectory);

        HttpRequest request = HttpRequest
                .newBuilder()
                .header("Content-Type", "text/html")
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://"+targetHost+":"+targetPort+"/sync"))
                .timeout(Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(sb.toString()))
                .build();
        CompletableFuture<HttpResponse<String>> cf = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
