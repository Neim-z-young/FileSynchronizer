package com.oyoungy.util;

/**
 * 用于实现具体的同步协议
 */
public interface SyncFileService {
    boolean syncFileByParam(SyncFileInfoForm form);
}
