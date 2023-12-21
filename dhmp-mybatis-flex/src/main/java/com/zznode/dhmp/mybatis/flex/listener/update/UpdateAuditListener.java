package com.zznode.dhmp.mybatis.flex.listener.update;

import com.mybatisflex.annotation.AbstractUpdateListener;
import com.zznode.dhmp.mybatis.flex.domain.BaseModel;
import com.zznode.dhmp.security.core.CustomUserDetails;
import com.zznode.dhmp.security.core.SecurityUtil;
import org.springframework.util.ClassUtils;

import java.util.Date;

/**
 * 实体类修改审计
 *
 * @author 王俊
 */
@SuppressWarnings("rawtypes")
public class UpdateAuditListener extends AbstractUpdateListener<BaseModel> {

    private static final Long DEFAULT_UPDATE_BY = 0L;

    private static final String SECURITY_UTIL_CLASS_NAME = "com.zznode.oauth2.core.SecurityUtil";
    private final boolean securityPresent = ClassUtils.isPresent(SECURITY_UTIL_CLASS_NAME, ClassUtils.getDefaultClassLoader());

    /**
     * 该监听器支持的 entity 类型。
     *
     * @return type
     */
    @Override
    public Class<BaseModel> supportType() {
        return BaseModel.class;
    }

    /**
     * 更新操作的前置操作。
     *
     * @param entity 实体类
     */
    @Override
    public void doUpdate(BaseModel entity) {

        Long id = DEFAULT_UPDATE_BY;
        if (securityPresent) {
            CustomUserDetails customUserDetails = SecurityUtil.getUserDetails();
            id = customUserDetails.getUserId();
        }
        entity.setLastUpdatedBy(id);
        entity.setLastUpdateDate(new Date());
    }
}
