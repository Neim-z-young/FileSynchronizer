package com.oyoungy.context;

import com.oyoungy.util.SyncFileInfoForm;

import java.lang.annotation.Target;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class ApplicationContext {
    private HttpClient httpClient;
    private Map<String, String> targetServers;
    private Map<String, String> sourceServers;

    public ApplicationContext(){
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofMillis(5000))
                .followRedirects(HttpClient.Redirect.NEVER)
                .executor(Executors.newFixedThreadPool(10))
                .build();

        targetServers = new HashMap<>();
        sourceServers = new HashMap<>();
    }

    public Map<String, String> getTargetServers() {
        return targetServers;
    }

    public Map<String, String> getSourceServers() {
        return sourceServers;
    }

    public HttpClient getHttpClient(){
        return httpClient;
    }


    public static String buildTargetServerKey(SyncFileInfoForm sFIF){
        if(sFIF.getTargetIp()==null) return null;

        return sFIF.getTargetIp()+":"+sFIF.getTargetPort()+";"+sFIF.getSourceFile();
    }

    public static String buildSourceServerKey(SyncFileInfoForm sFIF){
        if(sFIF.getSourceIp()==null) return null;

        return sFIF.getSourceIp()+":"+sFIF.getSourcePort()+";"+sFIF.getSourceFile()+";"+sFIF.getTargetDirectory();
    }
}
