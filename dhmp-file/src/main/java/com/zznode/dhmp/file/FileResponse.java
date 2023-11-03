package com.zznode.dhmp.file;

import org.springframework.util.Assert;

import java.io.InputStream;

/**
 * 获取文件的返回
 *
 * @author 王俊
 */
public class FileResponse {

    private Boolean success;

    private FileInfo fileInfo;

    private InputStream inputStream;

    private String errorMessage;

    private FileResponse() {
    }

    private FileResponse(FileInfo fileInfo) {
        this(fileInfo, "");
    }

    private FileResponse(FileInfo fileInfo, String errorMessage) {
        this.fileInfo = fileInfo;
        this.errorMessage = errorMessage;
        this.success = false;
    }

    private FileResponse(FileInfo fileInfo, InputStream inputStream) {
        Assert.notNull(fileInfo, "fileInfo cannot be null");
        Assert.notNull(inputStream, "inputStream cannot be null");
        this.fileInfo = fileInfo;
        this.inputStream = inputStream;
        this.success = true;
    }

    public static FileResponse success(FileInfo fileInfo, InputStream inputStream) {
        return new FileResponse(fileInfo, inputStream);
    }

    public static FileResponse failed(FileInfo fileInfo) {
        return new FileResponse(fileInfo);
    }

    public static FileResponse failed(FileInfo fileInfo, String errorMessage) {
        return new FileResponse(fileInfo, errorMessage);
    }


    public boolean success() {
        return this.success;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
