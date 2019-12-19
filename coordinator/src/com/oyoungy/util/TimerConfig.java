package com.oyoungy.util;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class TimerConfig {
    Logger logger = Logger.getLogger(TimerConfig.class.getName());

    public static final long WEEK_TIME = 1000 * 60 * 60 * 24 * 7;
    public static final long DAY_TIME = 1000 * 60 * 60 * 24;
    public static final long HOUR_TIME = 1000 * 60 * 60;
    public static final long MINUTE_TIME = 1000 * 60;
    public static final long SECOND_TIME = 1000;

    public static class SYNC_TYPE{
        public static final int INSTANT_SYNC = 0;
        public static final int MANUAL_SYNC = 1;
        public static final int DAY_PERIOD_SYNC = 2;
        public static final int WEEKLY_PERIOD_SYNC = 3;
        public static final int MONTHLY_PERIOD_SYNC = 4;
        public static final int WEEKDAY_SYNC = 5;
        public static final int MONTH_DAY_SYNC = 6;
        public static final int HOUR_PERIOD_SYNC = 7;
    }

    private  Map<String, TimerTask> timerTasks;
    private Timer timer;

    public String buildKey(FileSyncTaskParam param){
        return buildKey(param.getSourceIp(), param.getSourcePort(), param.getSourceFile(),
                param.getTargetIp(), param.getTargetPort(), param.getTargetDirectory());
    }

    public String buildKey(String sourceHost, String sourcePort, String sourceFile,
                           String targetHost, String targetPort, String targetDirectory){
        String key = sourceHost+":"+sourcePort+" "+sourceFile+";"+targetHost+":"+targetPort+" "+targetDirectory;
        logger.info("KEY is: " + key);
        return key;
    }

    public void addTimerConfig(String key, TimerTask task){
        if(timerTasks.containsKey(key) && !timerTasks.get(key).equals(task)){
            timerTasks.get(key).cancel();
        }
        timerTasks.put(key, task);
    }

    public void stopTimer(){
        timer.cancel();
        timerTasks.clear();
    }

    public FileSyncTimerTask buildTaskAndPushToSchedule(FileSyncForm fSF){
        String s = fSF.getSyncType(), p = fSF.getPeriod();
        FileSyncTimerTask task = null;
        int type = 0;
        long period = 0;
        logger.info("type: "+s+"  "+"period: "+p);
        try {
            if(s!=null){
                type = Integer.parseInt(s);
            }
            if(p!=null){
                period = Integer.parseInt(p);
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        if(type == SYNC_TYPE.INSTANT_SYNC){
            task = new FileSyncTimerTask(fSF.toTaskParam());
            period = 10 * SECOND_TIME;
        }
        logger.info(task.toString());
        timer.schedule(task, period);
        return task;
    }
}
