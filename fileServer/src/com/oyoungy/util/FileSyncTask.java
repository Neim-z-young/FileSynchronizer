package com.oyoungy.util;

import java.util.concurrent.Callable;

public class FileSyncTask implements Runnable{

    private SyncFileService syncFileService;
    private SyncFileInfoForm form;

    public FileSyncTask(SyncFileService syncFileService, SyncFileInfoForm form){

        this.syncFileService = syncFileService;
        this.form = form;
    }

    @Override
    public void run() {
        boolean res = syncFileService.syncFileByParam(form);
    }
}
