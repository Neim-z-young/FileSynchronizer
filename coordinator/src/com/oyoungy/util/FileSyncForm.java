package com.oyoungy.util;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class FileSyncForm {
    static Logger logger = Logger.getLogger(FileSyncForm.class.getName());

    private String sourceIp;
    private String sourcePort;
    private String sourcePath;
    private String sourceFile;
    private String targetIp;
    private String targetPort;
    private String targetPath;
    private String targetDir;

    private String syncType;
    private String period;

    public String getSourceIp() {
        return sourceIp;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public String getTargetPort() {
        return targetPort;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public String getSyncType() {
        return syncType;
    }

    public String getPeriod() {
        return period;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public void setTargetPort(String targetPort) {
        this.targetPort = targetPort;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public FileSyncTaskParam toTaskParam(){
        return new FileSyncTaskParam(
                sourceIp,
                sourcePort,
                sourcePath+"\\"+sourceFile,
                targetIp,
                targetPort,
                targetPath+"\\"+targetDir
        );
    }

    public static FileSyncForm parseHttpRequest(HttpServletRequest request) throws IllegalAccessException {
        Map<String, String[]> map = request.getParameterMap();
        Set<String> keys = map.keySet();
        for( String string : keys){
            logger.info("Map param: " + string + " : " + map.get(string)[0]);
        }
        FileSyncForm fileSyncForm = new FileSyncForm();
        Field[] fields = FileSyncForm.class.getDeclaredFields();
        for(Field field:fields){
            if(map.containsKey(field.getName())){
                StringBuilder sb = new StringBuilder();
                for(String s: map.get(field.getName())){
                    sb.append(s+"; ");
                }
                field.setAccessible(true);
                field.set(fileSyncForm, sb.toString());
            }
        }
        return fileSyncForm;
    }

    public static void main(String[] args) throws IllegalAccessException {
        Map<String, String[]> map = new HashMap<>();
        map.put("sourceIp", new String[]{"sd"});
        map.put("sourcePort", new String[]{"sd"});
        map.put("sourceFile", new String[]{"sd"});
        Set<String> keys = map.keySet();
        for( String string : keys){
            logger.info("Map param: " + string + " : " + map.get(string)[0]);
        }
        FileSyncForm fileSyncForm = new FileSyncForm();
        Field[] fields = FileSyncForm.class.getDeclaredFields();
        for(Field field:fields){
            logger.info("field: " + field.getName());
            if(map.containsKey(field.getName())){
                StringBuilder sb = new StringBuilder();
                for(String s: map.get(field.getName())){
                    logger.info("param: " + s);
                    sb.append(s);
                }
                field.setAccessible(true);
                field.set(fileSyncForm, sb.toString());
            }
        }
    }
}
