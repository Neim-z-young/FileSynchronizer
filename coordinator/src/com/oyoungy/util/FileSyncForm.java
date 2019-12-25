package com.oyoungy.util;


import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private String targetFile;

    private String syncType;
    private String option;

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

    public String getTargetFile() {
        return targetFile;
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

    public String getOption() {
        return option;
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

    public void setTargetFile(String targetFile) {
        this.targetFile = targetFile;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public FileSyncTaskParam toTaskParam(){
        Path source = Paths.get(sourcePath);
        source = source.resolve(sourceFile);
        Path target = Paths.get(targetPath);
        target = target.resolve(targetFile);

        return new FileSyncTaskParam(
                sourceIp,
                sourcePort,
                source.toString(),
                targetIp,
                targetPort,
                target.toString()
        );
    }

    public static FileSyncForm parseHttpRequest(HttpServletRequest request) throws IllegalAccessException, UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        Map<String, String[]> map = request.getParameterMap();
        Set<String> keys = map.keySet();
        for( String string : keys){
            logger.info("Map param: " + string + " : " + map.get(string)[0]);
        }
        FileSyncForm fileSyncForm = new FileSyncForm();
        Field[] fields = FileSyncForm.class.getDeclaredFields();
        for(Field field:fields){
            if(Modifier.isPrivate(field.getModifiers()) && map.containsKey(field.getName())){
                StringBuilder sb = new StringBuilder();
                for(String s: map.get(field.getName())){
                    sb.append(s);
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
