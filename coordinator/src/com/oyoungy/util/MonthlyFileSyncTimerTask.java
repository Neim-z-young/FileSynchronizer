package com.oyoungy.util;

import java.util.Calendar;

public class MonthlyFileSyncTimerTask extends FileSyncTimerTask {
    private int monthDay;

    public MonthlyFileSyncTimerTask(FileSyncTaskParam param) {
        super(param);
    }

    @Override
    protected boolean preValidate() {
        Calendar calendar = Calendar.getInstance();
        return monthDay == calendar.get(Calendar.DAY_OF_MONTH);
    }
}
