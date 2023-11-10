package com.zznode.dhmp.file;

import com.zznode.dhmp.file.exception.FileIOException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 描述
 *
 * @author 王俊
 */
public class DefaultFileClient implements FileClient, MessageSourceAware, InitializingBean {

    protected MessageSourceAccessor messages;

    public MessageSourceAccessor getMessages() {
        return messages;
    }

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
    @Override
    public FileInfo saveFile(String bucket, String objectName, InputStream inputStream) throws IOException {
        return saveFile(bucket, objectName, inputStream, false);
    }

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
    @Override
    public FileInfo saveFile(String bucket, String objectName, InputStream inputStream, boolean randomName) throws IOException {
        return null;
    }

    /**
     * 根据文件对象获取文件信息
     *
     * @param uid 文件对象uid
     * @return FileResponse包装
     * @see FileResponse
     */
    @Override
    public FileResponse getFile(String uid) {
        return null;
    }

    /**
     * 获取文件输入流
     *
     * @param bucket     桶名
     * @param objectName 文件对象名
     * @return 输入流
     * @throws IOException 文件不存在
     */
    @Override
    public InputStream getFile(String bucket, String objectName) throws IOException {
        return null;
    }

    /**
     * 下载文件到输出流
     *
     * @param bucket       桶名
     * @param objectName   文件对象名
     * @param outputStream 输出流
     * @throws FileIOException 抛出异常
     */
    @Override
    public void download(String bucket, String objectName, OutputStream outputStream) throws FileIOException {

    }

    /**
     * 下载文件到输出流
     *
     * @param uid          文件对象uid
     * @param outputStream 输出流
     * @throws FileIOException 抛出异常
     */
    @Override
    public void download(String uid, OutputStream outputStream) throws FileIOException {

    }

    /**
     * 下载文件到http响应
     *
     * @param bucket     桶名
     * @param objectName 文件对象名称
     * @param response   响应
     */
    @Override
    public void download(String bucket, String objectName, HttpServletResponse response) {

    }

    /**
     * 下载文件到http响应
     *
     * @param uid      文件对象uid
     * @param response 响应
     */
    @Override
    public void download(String uid, HttpServletResponse response) {

    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }


    @Override
    public void setMessageSource(MessageSource messageSource) {

    }
}
