package com.oyoungy.util;

import java.util.Calendar;

public class MonthlyFileSyncTimerTask extends FileSyncTimerTask {
    private int monthDay;

    public MonthlyFileSyncTimerTask(FileSyncTaskParam param, int monthDay) {
        super(param);
        if (monthDay < 1) {
            monthDay = 1;
        } else if (monthDay > 31) {
            monthDay = 31;
        }
        this.monthDay = monthDay;
    }

    @Override
    protected boolean preValidate() {
        Calendar calendar = Calendar.getInstance();
        return monthDay == calendar.get(Calendar.DAY_OF_MONTH);
    }
}
