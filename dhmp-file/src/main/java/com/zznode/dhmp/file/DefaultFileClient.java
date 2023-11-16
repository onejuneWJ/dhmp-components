package com.zznode.dhmp.file;

import cn.hutool.core.io.IORuntimeException;
import com.zznode.dhmp.core.exception.CommonException;
import com.zznode.dhmp.data.web.WebClientResponseUtil;
import com.zznode.dhmp.file.constant.FieldNames;
import com.zznode.dhmp.file.constant.FileErrorCode;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 默认的文件客户端,使用WebClient远程调用文件服务进行文件操作
 *
 * @author 王俊
 */
public class DefaultFileClient implements FileClient {

    /**
     * 文件服务地址
     */
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
        return saveFileAsync(bucket, objectName, inputStream, randomName).block();
    }

    /**
     * 异步保存文件
     *
     * @param bucket      保存至桶名
     * @param objectName  文件对象名
     * @param inputStream 输入流
     * @return 文件信息对象
     */
    @Override
    public Mono<FileInfo> saveFileAsync(String bucket, String objectName, InputStream inputStream) {
        return saveFileAsync(bucket, objectName, inputStream, false);
    }

    /**
     * 异步保存文件
     *
     * @param bucket      保存至桶名
     * @param objectName  文件对象名
     * @param inputStream 输入流
     * @param randomName  是否随机文件名称
     * @return 文件信息对象
     */
    @Override
    public Mono<FileInfo> saveFileAsync(String bucket, String objectName, InputStream inputStream, boolean randomName) {

        MultiValueMap<String, Object> multipartData = wrapFormData(bucket, objectName, inputStream, randomName);
        return webClient.post()
                .uri(this.baseUrl, uriBuilder -> uriBuilder.path("/v1/files").build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromMultipartData(multipartData))
                .exchangeToMono(clientResponse -> WebClientResponseUtil.mapToMono(clientResponse, FileInfo.class))
                .onErrorMap(WebClientResponseUtil::mapErr);
    }

    private MultiValueMap<String, Object> wrapFormData(String bucket, String objectName, InputStream inputStream, boolean randomName) {

        MultiValueMap<String, Object> multipartData = new LinkedMultiValueMap<>();
        multipartData.add(FieldNames.BUCKET, bucket);
        multipartData.add(FieldNames.OBJECT_NAME, objectName);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDispositionFormData(FieldNames.FILE, objectName);
        HttpEntity<InputStreamResource> httpEntity = new HttpEntity<>(new InputStreamResource(inputStream), httpHeaders);
        multipartData.add(FieldNames.FILE, httpEntity);
        multipartData.add(FieldNames.RANDOM_NAME, randomName);
        return multipartData;
    }

    /**
     * 根据文件对象获取文件信息
     *
     * @param uid 文件对象uid
     * @return 输入流
     */
    @Override
    public InputStream getFile(String uid) {
        Resource resource = this.webClient.get()
                .uri(this.baseUrl, uriBuilder -> uriBuilder.path("/v1/files/{uid}").build(uid))
                .exchangeToMono(clientResponse -> WebClientResponseUtil.mapToMono(clientResponse, Resource.class))
                .onErrorMap(WebClientResponseUtil::mapErr)
                .blockOptional()
                .orElseThrow(() -> new CommonException(FileErrorCode.FILE_INFO_NOT_FOUND, uid));
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            throw new IORuntimeException(e);
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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(FieldNames.BUCKET, bucket);
        params.add(FieldNames.OBJECT_NAME, objectName);
        Resource resource = this.webClient.get()
                .uri(this.baseUrl, uriBuilder -> uriBuilder
                        .path("/v1/files")
                        .queryParams(params)
                        .build()
                )
                .exchangeToMono(clientResponse -> WebClientResponseUtil.mapToMono(clientResponse, Resource.class))
                .onErrorMap(WebClientResponseUtil::mapErr)
                .blockOptional()
                .orElseThrow(() -> new CommonException(FileErrorCode.FILE_INFO_NOT_FOUND, bucket + File.separator + objectName));
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @Override
    public FileInfo getFileInfo(String uid) {
        return this.webClient.get()
                .uri(this.baseUrl, uriBuilder -> uriBuilder.path("/v1/file-infos/{uid}").build(uid))
                .exchangeToMono(clientResponse -> WebClientResponseUtil.mapToMono(clientResponse, FileInfo.class))
                .onErrorMap(WebClientResponseUtil::mapErr)
                .block();
    }


}
