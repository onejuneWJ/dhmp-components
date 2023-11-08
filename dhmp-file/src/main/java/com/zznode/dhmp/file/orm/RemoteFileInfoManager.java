package com.zznode.dhmp.file.orm;

import com.zznode.dhmp.core.constant.BaseConstants;
import com.zznode.dhmp.core.exception.CommonException;
import com.zznode.dhmp.data.exception.ExceptionResponse;
import com.zznode.dhmp.file.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

/**
 * 远程调用实现
 *
 * @author 王俊
 */
public class RemoteFileInfoManager implements FileInfoManager {

    private static final Logger logger = LoggerFactory.getLogger(RemoteFileInfoManager.class);
    private final RestTemplate restTemplate;

    private final URI fileServiceUrl;

    public RemoteFileInfoManager(RestTemplate restTemplate, String fileServiceUrl) {
        this(restTemplate, URI.create(fileServiceUrl));
    }

    public RemoteFileInfoManager(RestTemplate restTemplate, URI fileServiceUrl) {
        Assert.notNull(restTemplate, "restTemplate cannot be null");
        this.restTemplate = restTemplate;
        this.fileServiceUrl = fileServiceUrl;
    }

    @Override
    public FileInfo saveFileInfo(FileInfo fileInfo) throws RestClientException {
        RequestEntity<FileInfo> requestEntity = RequestEntity.post(fileServiceUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fileInfo);

        try {
            return this.restTemplate.exchange(requestEntity, FileInfo.class).getBody();
        } catch (RestClientException e) {
            if(e instanceof RestClientResponseException responseException){
                ExceptionResponse exceptionResponse = responseException.getResponseBodyAs(ExceptionResponse.class);
                throw new CommonException(Optional.ofNullable(exceptionResponse).map(ExceptionResponse::getMessage).orElse(BaseConstants.ErrorCode.ERROR), e);
            }
            throw e;
        }

    }

    @Override
    public FileInfo getFileInfo(String uid) throws RestClientException {
        RequestEntity<Void> requestEntity = RequestEntity.get(createUrlTemplate(), uid).build();
        try {
            return this.restTemplate.exchange(requestEntity, FileInfo.class).getBody();
        } catch (RestClientException e) {
            if(e instanceof RestClientResponseException responseException){
                ExceptionResponse exceptionResponse = responseException.getResponseBodyAs(ExceptionResponse.class);
                logger.error("error response message: {}", Optional.ofNullable(exceptionResponse).map(ExceptionResponse::getMessage).orElse(""));
                return null;
            }
            throw e;
        }
    }

    private String createUrlTemplate() {
        String path = this.fileServiceUrl.getPath();
        return this.fileServiceUrl + (path.endsWith("/") ? "{id}" : "/{id}");
    }
}
