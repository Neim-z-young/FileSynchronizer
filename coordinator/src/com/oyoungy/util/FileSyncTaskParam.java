package com.oyoungy.util;

public class FileSyncTaskParam {
    private String sourceIp;
    private String sourcePort;
    private String sourceFile;
    private String targetIp;
    private String targetPort;
    private String targetDirectory;

    public FileSyncTaskParam(String sourceIp, String sourcePort, String sourceFile, String targetIp, String targetPort, String targetDirectory) {
        this.sourceIp = sourceIp;
        this.sourcePort = sourcePort;
        this.sourceFile = sourceFile;
        this.targetIp = targetIp;
        this.targetPort = targetPort;
        this.targetDirectory = targetDirectory;
    }

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

    public String getTargetDirectory() {
        return targetDirectory;
    }
}
