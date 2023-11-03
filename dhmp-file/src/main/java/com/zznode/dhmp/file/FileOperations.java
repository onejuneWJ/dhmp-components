package com.zznode.dhmp.file;

import com.zznode.dhmp.file.exception.FileIOException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件操作
 *
 * @author 王俊
 */
public interface FileOperations {
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
     * @throws IOException 异常
     */
    FileInfo saveFile(String bucket, String objectName, InputStream inputStream, boolean randomName) throws IOException;

    /**
     * 根据文件对象获取文件信息
     *
     * @param uid 文件对象uid
     * @return FileResponse包装
     * @see FileResponse
     */
    FileResponse getFile(String uid);

    /**
     * 获取文件输入流
     *
     * @param bucket     桶名
     * @param objectName 文件对象名
     * @return 输入流
     * @throws IOException 文件不存在
     */
    InputStream getFile(String bucket, String objectName) throws IOException;

    /**
     * 下载文件到输出流
     *
     * @param bucket       桶名
     * @param objectName   文件对象名
     * @param outputStream 输出流
     * @throws FileIOException 抛出异常
     */
    void download(String bucket, String objectName, OutputStream outputStream) throws FileIOException;

    /**
     * 下载文件到输出流
     *
     * @param uid          文件对象uid
     * @param outputStream 输出流
     * @throws FileIOException 抛出异常
     */
    void download(String uid, OutputStream outputStream) throws FileIOException;

    /**
     * 下载文件到http响应
     *
     * @param bucket     桶名
     * @param objectName 文件对象名称
     * @param response   响应
     */
    void download(String bucket, String objectName, HttpServletResponse response);

    /**
     * 下载文件到http响应
     *
     * @param uid      文件对象uid
     * @param response 响应
     */
    void download(String uid, HttpServletResponse response);
}
