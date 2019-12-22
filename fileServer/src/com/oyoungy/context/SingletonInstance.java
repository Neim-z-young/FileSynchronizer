package com.oyoungy.context;

import com.oyoungy.util.HttpFileSyncer;
import com.oyoungy.util.SyncFileService;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SingletonInstance {
   private static ApplicationContext context = new ApplicationContext();

   private static SyncFileService service = new HttpFileSyncer();

   private static ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

   public static ApplicationContext getAppContext(){
       return context;
   }

   public static SyncFileService getSyncService(){
       return service;
   }

   public static ThreadPoolExecutor getPoolExecutor(){
       return poolExecutor;
   }
}
