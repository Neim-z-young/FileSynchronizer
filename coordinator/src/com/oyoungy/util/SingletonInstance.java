package com.oyoungy.util;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;

public class SingletonInstance {
    private static TimerConfig timerConfigContext = new TimerConfig();
    private static HttpClient httpClient;
    static {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofMillis(5000))
                .followRedirects(HttpClient.Redirect.NEVER)
                .executor(Executors.newFixedThreadPool(10))
                .build();
    }

    public static TimerConfig getTimerContext(){
        return timerConfigContext;
    }

    public static HttpClient getHttpClientContext(){
        return httpClient;
    }
}
