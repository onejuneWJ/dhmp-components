package com.zznode.dhmp.file;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作
 *
 * @author 王俊
 */
public interface FileClient {
    /**
     * 保存文件
     *
     * @param bucket      保存至桶名
     * @param objectName  文件对象名
     * @param inputStream 输入流
     * @return 文件信息对象
     * @throws IOException 异常
     * @see #saveFile(String, String, InputStream, boolean)
     */
    FileInfo saveFile(String bucket, String objectName, InputStream inputStream) throws IOException;

    /**
     * 保存文件
     *
     * @param bucket      保存至桶名
     * @param objectName  文件对象名
     * @param inputStream 输入流
     * @param randomName  是否随机文件名称
     * @return 文件信息对象
     */
    FileInfo saveFile(String bucket, String objectName, InputStream inputStream, boolean randomName);

    /**
     * 根据文件对象id获取文件信息
     *
     * @param uid 文件对象uid
     * @return 输入流
     */
    InputStream getFile(String uid);

    /**
     * 获取文件输入流
     *
     * @param bucket     桶名
     * @param objectName 文件对象名
     * @return 输入流
     */
    InputStream getFile(String bucket, String objectName);

    /**
     * 根据文件对象id获取文件信息
     *
     * @param uid 文件对象uid
     * @return FileInfo/null
     */
    @Nullable
    FileInfo getFileInfo(String uid);

    /**
     * 获取文件信息
     *
     * @param bucket     桶名
     * @param objectName 文件对象名
     * @return FileInfo/null
     */
    @Nullable
    FileInfo getFileInfo(String bucket, String objectName);

    /**
     * 下载文件到http响应
     *
     * @param bucket     桶名
     * @param objectName 文件对象名称
     * @param response   响应
     */
    void download(String bucket, String objectName, HttpServletResponse response, @Nullable String filename);

    /**
     * 下载文件到http响应
     *
     * @param uid      文件对象uid
     * @param response 响应
     */
    void download(String uid, HttpServletResponse response, @NonNull String filename);
}
