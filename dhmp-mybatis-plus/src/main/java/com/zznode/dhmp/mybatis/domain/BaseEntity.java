package com.zznode.dhmp.mybatis.domain;

import com.baomidou.mybatisplus.annotation.Version;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体，维护了继承字段
 * <p>所有实体类都应该继承该类
 *
 * @author 王俊
 * @date create in 2023/7/4 15:28
 */
public class BaseEntity implements Serializable {

    public static final String FIELD_CREATION_DATE = "creation_date";
    public static final String FIELD_CREATED_BY = "created_by";
    public static final String FIELD_LAST_UPDATE_DATE = "last_update_date";
    public static final String FIELD_LAST_UPDATED_BY = "last_updated_by";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "object_version_number";

    private Date creationDate;
    private Long createdBy;
    private Date lastUpdateDate;
    private Long lastUpdatedBy;
    @Version
    private Long objectVersionNumber;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }
}
