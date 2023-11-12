package com.zznode.dhmp.file;

import com.zznode.dhmp.core.exception.CommonException;
import com.zznode.dhmp.core.message.DhmpMessageSource;
import com.zznode.dhmp.file.constant.FieldNames;
import com.zznode.dhmp.file.constant.FileErrorCode;
import com.zznode.dhmp.file.util.DownloadUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * 默认的文件客户端,使用WebClient远程调用文件服务进行文件操作
 *
 * @author 王俊
 */
public class DefaultFileClient implements FileClient, MessageSourceAware, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected MessageSourceAccessor messages;

    private final String baseUrl;

    private final WebClient webClient;

    public DefaultFileClient(WebClient webClient, String fileServiceUrl) {
        this.webClient = webClient;
        this.baseUrl = fileServiceUrl;
    }


    /**
     * 保存文件
     *
     * @param bucket      保存至桶名
     * @param objectName  文件对象名
     * @param inputStream 输入流
     * @return 文件信息对象
     * @see #saveFile(String, String, InputStream, boolean)
     */
    @Override
    public FileInfo saveFile(String bucket, String objectName, InputStream inputStream) {
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
     */
    @Override
    public FileInfo saveFile(String bucket, String objectName, InputStream inputStream, boolean randomName) {
        MultiValueMap<String, Object> multipartData = new LinkedMultiValueMap<>();
        multipartData.add(FieldNames.BUCKET, bucket);
        multipartData.add(FieldNames.OBJECT_NAME, objectName);
        multipartData.add(FieldNames.FILE, new InputStreamResource(inputStream));
        return webClient.post()
                .uri(this.baseUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromMultipartData(multipartData))
                .exchangeToMono(clientResponse -> clientResponse
                        .bodyToMono(FileInfo.class)
                        .doOnError(throwable -> {
                            throw new RuntimeException(throwable);
                        })
                )
                .block();
    }

    /**
     * 根据文件对象获取文件信息
     *
     * @param uid 文件对象uid
     * @return FileResponse包装
     * @see FileResponse
     */
    @Override
    public InputStream getFile(String uid) {

        try {
            return this.webClient.get()
                    .uri(this.baseUrl, uriBuilder -> uriBuilder.path("/{uid}").build(uid))
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(InputStreamSource.class))
                    .blockOptional()
                    .orElseThrow(() -> new CommonException(FileErrorCode.FILE_INFO_NOT_FOUND))
                    .getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件输入流
     *
     * @param bucket     桶名
     * @param objectName 文件对象名
     * @return 输入流
     */
    @Override
    public InputStream getFile(String bucket, String objectName) {
        MultiValueMap<String, String> multipartData = new LinkedMultiValueMap<>();
        multipartData.add(FieldNames.BUCKET, bucket);
        multipartData.add(FieldNames.OBJECT_NAME, objectName);
        return this.webClient.get()
                .uri(this.baseUrl, uriBuilder -> uriBuilder.queryParams(multipartData).build())
                .retrieve()
                .bodyToMono(InputStreamSource.class)
                .blockOptional()
                .map(resource -> {
                    try {
                        return resource.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new CommonException(FileErrorCode.FILE_INFO_NOT_FOUND));
    }

    @Override
    public FileInfo getFileInfo(String uid) {
        return this.webClient.get()
                .uri(this.baseUrl, uriBuilder -> uriBuilder.path("/{uid}").build(uid))
                .retrieve()
                .bodyToMono(FileInfo.class)
                .block();
    }

    @Override
    public FileInfo getFileInfo(String bucket, String objectName) {

        return this.webClient.get()
                .uri(this.baseUrl, uriBuilder -> uriBuilder
                        .queryParam(FieldNames.BUCKET, bucket)
                        .queryParam(FieldNames.OBJECT_NAME, objectName)
                        .build()
                )
                .retrieve()
                .bodyToMono(FileInfo.class)
                .block();
    }


    /**
     * 下载文件到http响应
     *
     * @param bucket     桶名
     * @param objectName 文件对象名称
     * @param response   响应
     */
    @Override
    public void download(String bucket, String objectName, HttpServletResponse response, String filename) {
        if (!StringUtils.hasText(filename)) {
            filename = Path.of(objectName).getFileName().toString();
        }
        DownloadUtil.download(getFile(bucket, objectName), response, filename);
    }

    /**
     * 下载文件到http响应
     *
     * @param uid      文件对象uid
     * @param response 响应
     */
    @Override
    public void download(String uid, HttpServletResponse response, @NonNull String filename) {
        Assert.hasText(filename, "filename must provide for download");
        DownloadUtil.download(getFile(uid), response, filename);

    }


    @Override
    public void afterPropertiesSet() {
        DhmpMessageSource.addBasename("messages.messages_file");
    }


    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {

        this.messages = new MessageSourceAccessor(messageSource);
    }
}
