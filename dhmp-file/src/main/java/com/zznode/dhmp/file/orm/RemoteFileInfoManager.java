package com.zznode.dhmp.file.orm;

import com.zznode.dhmp.file.FileInfo;
import com.zznode.dhmp.file.orm.FileInfoManager;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 远程调用实现
 *
 * @author 王俊
 */
public class RemoteFileInfoManager implements FileInfoManager {

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
        return this.restTemplate.postForObject(fileServiceUrl, fileInfo, FileInfo.class);
    }

    @Override
    public FileInfo getFileInfo(String uid) throws RestClientException {
        Map<String, Object> variables = new HashMap<>(1);
        variables.put("uid", uid);
        return this.restTemplate.getForObject(this.fileServiceUrl.toString(), FileInfo.class, variables);
    }

    @Override
    public FileInfo getFileInfo(Long id) {
        URI uri = this.fileServiceUrl.resolve(String.valueOf(id));
        return this.restTemplate.getForObject(uri, FileInfo.class);
    }
}
