package com.oyoungy.util;

import java.util.Calendar;

public class WeeklyFileSyncTimerTask extends FileSyncTimerTask {
    private int weekDay;

    public WeeklyFileSyncTimerTask(FileSyncTaskParam param) {
        super(param);
    }


    @Override
    protected boolean preValidate() {
        Calendar calendar = Calendar.getInstance();
        return weekDay == calendar.get(Calendar.DAY_OF_WEEK);
    }
}
