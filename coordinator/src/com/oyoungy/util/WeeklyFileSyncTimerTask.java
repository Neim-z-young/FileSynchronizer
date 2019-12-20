package com.oyoungy.util;

import java.util.Calendar;

public class WeeklyFileSyncTimerTask extends FileSyncTimerTask {
    private int weekDay;

    public WeeklyFileSyncTimerTask(FileSyncTaskParam param, int weekDay) {
        super(param);
        if (weekDay < 1) {
            weekDay = 1;
        }else if (weekDay > 7) {
            weekDay = 7;
        }
        this.weekDay = weekDay;
    }


    @Override
    protected boolean preValidate() {
        Calendar calendar = Calendar.getInstance();
        return weekDay == calendar.get(Calendar.DAY_OF_WEEK);
    }
}
