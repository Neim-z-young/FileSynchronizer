package com.oyoungy.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class TimerConfig {
    Logger logger = Logger.getLogger(TimerConfig.class.getName());

    public static final long MONTH_TIME = 2592000000L;  //1000 * 60 * 60 * 24 * 30
    public static final long WEEK_TIME = 1000 * 60 * 60 * 24 * 7;
    public static final long DAY_TIME = 1000 * 60 * 60 * 24;
    public static final long HOUR_TIME = 1000 * 60 * 60;
    public static final long MINUTE_TIME = 1000 * 60;
    public static final long SECOND_TIME = 1000;

    /**
     * 同步方式，有些类型需要额外的参数
     */
    public static class SYNC_TYPE{
        public static final int INSTANT_SYNC = 0;
        public static final int MANUAL_SYNC = 1;
        public static final int DAY_PERIOD_SYNC = 2;
        public static final int WEEKLY_PERIOD_SYNC = 3;
        public static final int MONTHLY_PERIOD_SYNC = 4;
        public static final int WEEKDAY_SYNC = 5;
        public static final int MONTH_DAY_SYNC = 6;
        public static final int HOUR_PERIOD_SYNC = 7;
        public static final int DAYTIME_SYNC = 8;
    }

    private  Map<String, TimerTask> timerTasks;
    private Timer timer;

    public TimerConfig(){
        timer = new Timer();
        timerTasks = new HashMap<>();
    }

    public String buildKey(FileSyncTaskParam param){
        return buildKey(param.getSourceIp(), param.getSourcePort(), param.getSourceFile(),
                param.getTargetIp(), param.getTargetPort(), param.getTargetFile());
    }

    public String buildKey(String sourceHost, String sourcePort, String sourceFile,
                           String targetHost, String targetPort, String targetFile){
        String key = sourceHost+":"+sourcePort+";"+sourceFile+";"+targetHost+":"+targetPort+";"+targetFile;
        logger.info("KEY is: " + key);
        return key;
    }

    public void addTimerConfig(String key, TimerTask task){
        if(timerTasks.containsKey(key) && !timerTasks.get(key).equals(task)){
            logger.info("Canceling existed task");
            timerTasks.get(key).cancel();
        }
        timerTasks.put(key, task);
    }

    public void stopTimer(){
        timer.cancel();
        timerTasks.clear();
    }

    public FileSyncTimerTask buildTaskAndPushToSchedule(FileSyncForm fSF){
        String s = fSF.getSyncType(), p = fSF.getOption();
        FileSyncTimerTask task = null;
        int type = 0;
        long delay = 0, period = -1;
        int option = 0;
        logger.info("type: "+s+"  "+"period: "+p);
        try {
            if(s!=null){
                type = Integer.parseInt(s);
            }
            if(p!=null){
                option = Integer.parseInt(p);
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        //下一天的0点
        long nextZeroTime = TimeHelper.getZeroStampOfCurrentDay() + DAY_TIME;

        if(type == SYNC_TYPE.INSTANT_SYNC){
            task = new FileSyncTimerTask(fSF.toTaskParam());
            period = 10 * SECOND_TIME;
        }else if(type == SYNC_TYPE.MANUAL_SYNC){
            task = new FileSyncTimerTask(fSF.toTaskParam());
            period = -1;
        }else if(type == SYNC_TYPE.DAY_PERIOD_SYNC){
            task = new FileSyncTimerTask(fSF.toTaskParam());
            period = option * DAY_TIME;
        }else if(type == SYNC_TYPE.WEEKLY_PERIOD_SYNC){
            task = new FileSyncTimerTask(fSF.toTaskParam());
            period = option * WEEK_TIME;
        }else if(type == SYNC_TYPE.MONTHLY_PERIOD_SYNC){
            task = new FileSyncTimerTask(fSF.toTaskParam());
            period = option * MONTH_TIME;
        }else if(type == SYNC_TYPE.WEEKDAY_SYNC){
            task = new WeeklyFileSyncTimerTask(fSF.toTaskParam(), option);
            delay = nextZeroTime;
            period = DAY_TIME;
        }else if(type == SYNC_TYPE.MONTH_DAY_SYNC){
            task = new MonthlyFileSyncTimerTask(fSF.toTaskParam(), option);
            delay = nextZeroTime;
            period = DAY_TIME;
        }else if(type == SYNC_TYPE.HOUR_PERIOD_SYNC){
            task = new FileSyncTimerTask(fSF.toTaskParam());
            period = option * DAY_TIME;
        }else if(type == SYNC_TYPE.DAYTIME_SYNC){
            task = new DailyFileSyncTimerTask(fSF.toTaskParam(), option);
            period = HOUR_TIME;
        }else {

        }

        if(task!=null){
            try {
                logger.info(task.toString());
                if(period<=0){
                    timer.schedule(task, delay);
                }else {
                    timer.schedule(task, delay ,period);
                }
            }catch (Exception e){
                logger.warning("task schedule error!!!");
                task.cancel();
            }
        }
        return task;
    }
}
