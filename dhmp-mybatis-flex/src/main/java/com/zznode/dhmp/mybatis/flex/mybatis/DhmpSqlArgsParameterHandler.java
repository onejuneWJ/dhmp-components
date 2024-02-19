/*
 *  Copyright (c) 2022-2023, Mybatis-Flex (fuhai999@gmail.com).
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.zznode.dhmp.mybatis.flex.mybatis;

import com.mybatisflex.core.FlexConsts;
import com.mybatisflex.core.mybatis.TypeHandlerObject;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 重写mybatis-flex的{@link com.mybatisflex.core.mybatis.SqlArgsParameterHandler }，以兼容PageHelper的参数
 */
public class DhmpSqlArgsParameterHandler implements ParameterHandler {

    private final TypeHandlerRegistry typeHandlerRegistry;

    private final MappedStatement mappedStatement;
    private final Map parameterObject;
    private final BoundSql boundSql;
    private final Configuration configuration;

    public DhmpSqlArgsParameterHandler(MappedStatement mappedStatement, Map parameterObject, BoundSql boundSql) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
    }


    @Override
    public Object getParameterObject() {
        return parameterObject;
    }

    @Override
    public void setParameters(PreparedStatement ps) {
        try {
            doSetParameters(ps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void doSetParameters(PreparedStatement ps) throws SQLException {
        Object[] sqlArgs = (Object[]) ((Map<?, ?>) parameterObject).get(FlexConsts.SQL_ARGS);
        int index = 1;
        // 如果存在SQL_ARGS，说明使用的是QueryWrapper进行的查询
        if (sqlArgs != null) {
            for (Object value : sqlArgs) {
                //通过配置的 TypeHandler 去设置内容
                if (value instanceof TypeHandlerObject) {
                    ((TypeHandlerObject) value).setParameter(ps, index++);
                }
                //在 Oracle、SqlServer 中 TIMESTAMP、DATE 类型的数据是支持 java.util.Date 给值的
                else if (value instanceof Date) {
                    setDateParameter(ps, (Date) value, index++);
                } else if (value instanceof byte[]) {
                    ps.setBytes(index++, (byte[]) value);
                } else {
                    /** 在 MySql，Oracle 等驱动中，通过 PreparedStatement.setObject 后，驱动会自动根据 value 内容进行转换
                     * 源码可参考： {{@link com.mysql.jdbc.PreparedStatement#setObject(int, Object)}
                     **/
                    ps.setObject(index++, value);
                }
            }
        }
        // 这里分三种情况
        // 1. QueryWrapper查询，但未使用pagehelper分页。getParameterMappings()一般都为空，不影响。
        // 2. QueryWrapper查询，使用pagehelper分页。需要继续执行下面的代码，设置分页参数到ps
        // 3. 不是QueryWrapper，上面的代码不会执行。参数由下面的代码设置到ps
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            MetaObject metaObject = null;
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        if (metaObject == null) {
                            metaObject = configuration.newMetaObject(parameterObject);
                        }
                        value = metaObject.getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (value == null && jdbcType == null) {
                        jdbcType = configuration.getJdbcTypeForNull();
                    }
                    try {
                        // 继续上次的index
                        typeHandler.setParameter(ps, index++, value, jdbcType);
                    } catch (TypeException | SQLException e) {
                        throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
                    }
                }
            }
        }

    }

    /**
     * Oracle、SqlServer 需要主动设置下 date 类型
     * MySql 通过 setObject 后会自动转换，具体查看 MySql 驱动源码
     *
     * @param ps    PreparedStatement
     * @param value date value
     * @param index set to index
     * @throws SQLException
     */
    private void setDateParameter(PreparedStatement ps, Date value, int index) throws SQLException {
        if (value instanceof java.sql.Date) {
            ps.setDate(index, (java.sql.Date) value);
        } else if (value instanceof java.sql.Timestamp) {
            ps.setTimestamp(index, (java.sql.Timestamp) value);
        } else {
            ps.setTimestamp(index, new java.sql.Timestamp(value.getTime()));
        }
    }

}
