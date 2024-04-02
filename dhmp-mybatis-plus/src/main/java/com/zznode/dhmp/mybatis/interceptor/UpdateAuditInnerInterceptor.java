package com.zznode.dhmp.mybatis.interceptor;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.zznode.dhmp.mybatis.domain.BaseEntity;
import com.zznode.dhmp.security.core.CustomUserDetails;
import com.zznode.dhmp.security.core.SecurityUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.util.ClassUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * 修改操作修改audit信息
 *
 * @author 王俊 2021/04/22 15:35
 */
@SuppressWarnings({"unchecked"})
public class UpdateAuditInnerInterceptor implements InnerInterceptor {
    private static final Long DEFAULT_UPDATE_BY = 0L;

    private static final String SECURITY_UTIL_CLASS_NAME = "com.zznode.oauth2.core.SecurityUtil";

    private static final boolean securityUtilPresent = ClassUtils.isPresent(SECURITY_UTIL_CLASS_NAME, ClassUtils.getDefaultClassLoader());

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        if (SqlCommandType.UPDATE != ms.getSqlCommandType()) {
            return;
        }
        if (parameter instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) parameter;
            initCreation(map);
        }
    }

    private void initCreation(Map<String, Object> map) {
        Object et = map.getOrDefault(Constants.ENTITY, null);
        if (et instanceof BaseEntity baseEntity) {
            Long id = DEFAULT_UPDATE_BY;
            if (securityUtilPresent) {
                CustomUserDetails customUserDetails = SecurityUtil.getUserDetails();
                id = customUserDetails.getUserId();
            }
            baseEntity.setLastUpdatedBy(id);
            baseEntity.setLastUpdateDate(new Date());
        }
    }
}
