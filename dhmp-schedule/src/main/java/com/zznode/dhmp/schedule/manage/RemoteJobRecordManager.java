package com.zznode.dhmp.schedule.manage;

import cn.hutool.core.date.DateUtil;
import com.zznode.dhmp.schedule.constants.JobRecordFields;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * 远程调用，添加任务执行记录
 *
 * @author 王俊
 */
public class RemoteJobRecordManager implements JobRecordManager {

    private String serverUrl = "http://localhost:8100";

    private final RestClient restClient;

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public RemoteJobRecordManager(RestClient restClient) {
        this.restClient = restClient;
    }


    /**
     * @param jobCode 任务编码
     * @param result  1/0
     */
    @Override
    public void addJobRecord(String jobCode, Integer result) {
        addJobRecord(jobCode, result, null);
    }

    /**
     * @param jobCode 任务编码
     * @param result  1/0
     * @param message 失败原因
     */
    @Override
    public void addJobRecord(String jobCode, Integer result, String message) {

        restClient.post()
                .uri(serverUrl, uriBuilder -> uriBuilder.path("/v1/schedule-task-records").build())
                .body(createRequestBody(jobCode, result, message))
                .retrieve()
                .body(Object.class);
    }

    private Map<String, Object> createRequestBody(String jobCode, Integer result, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put(JobRecordFields.JOB_CODE, jobCode);
        map.put(JobRecordFields.RESULT, result);
        map.put(JobRecordFields.FAILED_REASON, message);
        map.put(JobRecordFields.EXE_TIME, DateUtil.now());
        return map;
    }
}
