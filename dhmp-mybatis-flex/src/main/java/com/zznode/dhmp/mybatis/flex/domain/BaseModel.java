package com.zznode.dhmp.mybatis.flex.domain;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.activerecord.Model;

import java.util.Date;

/**
 * 基础实体，维护了继承字段
 * <p>所有实体类都应该继承该类
 *
 * @author 王俊
 */
public class BaseModel<T extends Model<T>> extends Model<T> {


    public static final String FIELD_CREATION_DATE = "creation_date";
    public static final String FIELD_CREATED_BY = "created_by";
    public static final String FIELD_LAST_UPDATE_DATE = "last_update_date";
    public static final String FIELD_LAST_UPDATED_BY = "last_updated_by";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "object_version_number";

    private Date creationDate;
    private Long createdBy;
    private Date lastUpdateDate;
    private Long lastUpdatedBy;

    @Column(version = true)
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
