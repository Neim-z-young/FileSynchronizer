package com.oyoungy.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SyncFileInfoForm {
    public static Logger logger = Logger.getLogger(SyncFileInfoForm.class.getName());
    private String sourceIp;
    private String sourcePort;
    private String sourceFile;
    private String targetDirectory;

    public static SyncFileInfoForm parseHttpRequest(HttpServletRequest request) throws IllegalAccessException {

        String rowLine = "";
        try {
            BufferedReader reader = request.getReader();
             rowLine = reader.readLine();
        } catch (IOException e) {
            logger.warning("IO Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        int cur = 0, next;
        //TODO 修正硬编码
        String sep = ": ", sep2 = ", ";
        Map<String, String> map = new HashMap<>();
        while (cur!=rowLine.length()){
            next = rowLine.indexOf(sep, cur);
            String key = rowLine.substring(cur, next);
            cur = next + sep.length();
            next = rowLine.indexOf(sep2, cur);
            String value = rowLine.substring(cur, next);
            cur  = next + sep2.length();
            map.put(key, value);
        }

        SyncFileInfoForm form = new SyncFileInfoForm();
        Field[] fields = SyncFileInfoForm.class.getDeclaredFields();
        for(Field field : fields){
            logger.info("field: " + field.getName());
            if(map.containsKey(field.getName())){
                String s = map.get(field.getName());
                logger.info("param: " + s);
                field.setAccessible(true);
                field.set(form, s);
            }
        }
        return form;
    }
    public static void main(String[] args) throws IllegalAccessException {
        String rowLine = "sourceIp: dhasjkd, sourcePort: czxcvf, sourceFile: hvoqng, targetDirectory: djjnvpq, ";
        int cur = 0, next;
        //TODO 修正硬编码
        String sep = ": ", sep2 = ", ";
        Map<String, String> map = new HashMap<>();
        while (cur!=rowLine.length()){
            next = rowLine.indexOf(sep, cur);
            String key = rowLine.substring(cur, next);
            cur = next + sep.length();
            next = rowLine.indexOf(sep2, cur);
            String value = rowLine.substring(cur, next);
            cur  = next + sep2.length();
            map.put(key, value);
        }

        SyncFileInfoForm form = new SyncFileInfoForm();
        Field[] fields = SyncFileInfoForm.class.getDeclaredFields();
        for(Field field : fields){
            logger.info("field: " + field.getName());
            if(map.containsKey(field.getName())){
                String s = map.get(field.getName());
                logger.info("param: " + s);
                field.setAccessible(true);
                field.set(form, s);
            }
        }
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }
}
