package com.zznode.dhmp.mybatis.interceptor;

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

/**
 * 数据插入初始化拦截器
 * <p>初始化创建时间、创建人等字段</p>
 *
 * @author 王俊 2021/04/22 15:28
 */
public class InsertAuditInnerInterceptor implements InnerInterceptor {

    private static final Long DEFAULT_CREATED_BY = 0L;

    private static final String SECURITY_UTIL_CLASS_NAME = "com.zznode.oauth2.core.SecurityUtil";
    private final boolean securityPresent = ClassUtils.isPresent(SECURITY_UTIL_CLASS_NAME, ClassUtils.getDefaultClassLoader());

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        if (SqlCommandType.INSERT != ms.getSqlCommandType()) {
            return;
        }
        initCreation(parameter);
    }

    private void initCreation(Object parameter) {
        if (parameter instanceof BaseEntity baseEntity) {
            Long id = DEFAULT_CREATED_BY;
            // todo need security
            if (securityPresent) {
                CustomUserDetails customUserDetails = SecurityUtil.getUserDetails();
                id = customUserDetails.getUserId();
            }
            baseEntity.setCreatedBy(id);
            baseEntity.setCreationDate(new Date());
            baseEntity.setLastUpdatedBy(id);
            baseEntity.setLastUpdateDate(new Date());
            baseEntity.setObjectVersionNumber(0L);
        }
    }

}
