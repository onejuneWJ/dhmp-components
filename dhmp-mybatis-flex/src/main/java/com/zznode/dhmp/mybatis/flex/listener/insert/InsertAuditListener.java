package com.zznode.dhmp.mybatis.flex.listener.insert;

import com.mybatisflex.annotation.AbstractInsertListener;
import com.zznode.dhmp.mybatis.flex.annotation.ModifyAudit;
import com.zznode.dhmp.mybatis.flex.domain.BaseModel;
import com.zznode.dhmp.security.core.CustomUserDetails;
import com.zznode.dhmp.security.core.SecurityUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.util.Date;

/**
 * 实体类新建审计
 *
 * @author 王俊
 */
@SuppressWarnings("rawtypes")
public class InsertAuditListener extends AbstractInsertListener<BaseModel> {

    private static final Long DEFAULT_CREATED_BY = 0L;

    private static final String SECURITY_UTIL_CLASS_NAME = "com.zznode.dhmp.security.core.SecurityUtil";
    private final boolean securityPresent = ClassUtils.isPresent(SECURITY_UTIL_CLASS_NAME, ClassUtils.getDefaultClassLoader());

    /**
     * 新增操作的前置操作。
     *
     * @param entity 实体类
     */
    @Override
    public void doInsert(BaseModel entity) {
        ModifyAudit annotation = AnnotationUtils.findAnnotation(entity.getClass(), ModifyAudit.class);
        if (annotation == null) {
            return;
        }
        Long id = DEFAULT_CREATED_BY;
        if (securityPresent) {
            CustomUserDetails customUserDetails = SecurityUtil.getUserDetails();
            id = customUserDetails.getUserId();
        }
        entity.setCreatedBy(id);
        entity.setCreationDate(new Date());
        entity.setLastUpdatedBy(id);
        entity.setLastUpdateDate(new Date());
        entity.setObjectVersionNumber(0L);
    }
}
