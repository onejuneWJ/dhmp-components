package com.zznode.dhmp.file;

import cn.hutool.core.util.RandomUtil;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * 文件信息
 *
 * @author 王俊
 */
public class FileInfo implements Serializable {
    /**
     * 唯一ID
     */
    private String uid;
    /**
     * 桶名
     */
    private String bucket;
    /**
     * 对象名
     * <p> 如：/path/to/file.txt
     */
    private String objectName;
    /**
     * 文件名称
     * <p> file.txt
     */
    private String fileName;

    public FileInfo() {
    }

    public FileInfo(String bucket, String objectName) {
        this.bucket = bucket;
        this.objectName = objectName;
    }

    public FileInfo(String bucket, String objectName, String fileName) {
        this.bucket = bucket;
        this.objectName = objectName;
        this.fileName = fileName;
    }

    public static FileInfo of(String bucket, Path namePath) {

        String fileName = namePath.getFileName().toString();
        String objectName = namePath.toString();
        FileInfo fileInfo = new FileInfo(bucket, objectName, fileName);
        fileInfo.generateUid();
        return fileInfo;
    }

    public void generateUid() {
        setUid(RandomUtil.randomString(32));
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
