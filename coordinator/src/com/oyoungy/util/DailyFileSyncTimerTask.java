package com.oyoungy.util;

import java.util.Calendar;

public class DailyFileSyncTimerTask extends FileSyncTimerTask {
    private int daytime;

    public DailyFileSyncTimerTask(FileSyncTaskParam param, int daytime) {
        super(param);
        if (daytime < 0) {
            daytime = 0;
        } else if (daytime > 23) {
            daytime = 23;
        }
        this.daytime = daytime;
    }

    @Override
    protected boolean preValidate() {
        Calendar calendar = Calendar.getInstance();
        return daytime == calendar.get(Calendar.HOUR_OF_DAY);
    }
}
