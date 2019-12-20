package com.oyoungy.util;

import javax.servlet.http.HttpServletRequest;

public class SyncFileInfoForm {
    private String sourceIp;
    private String sourcePort;
    private String sourceFile;
    private String targetDirectory;

    public static SyncFileInfoForm parseHttpRequest(HttpServletRequest request){
        return null;
    }
}
