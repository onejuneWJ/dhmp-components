package com.zznode.dhmp.mybatis.flex.mybatis;

import com.mybatisflex.core.FlexConsts;
import com.mybatisflex.core.mybatis.FlexConfiguration;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

/**
 * 重写FlexConfiguration，使用{@link DhmpSqlArgsParameterHandler}
 *
 * @author 王俊
 */
public class DhmpFlexConfiguration extends FlexConfiguration {

    @Override
    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        String mappedStatementId = mappedStatement.getId();
        /**
         *  以 "!selectKey" 结尾的 mappedStatementId，是用于 Sequence 生成主键的，无需为其设置参数
         *  {@link SelectKeyGenerator#SELECT_KEY_SUFFIX}
         */
        if (!mappedStatementId.endsWith(SelectKeyGenerator.SELECT_KEY_SUFFIX)
                && parameterObject instanceof Map
                && ((Map<?, ?>) parameterObject).containsKey(FlexConsts.SQL_ARGS)) {
            // 这里修改为DhmpSqlArgsParameterHandler
            DhmpSqlArgsParameterHandler sqlArgsParameterHandler = new DhmpSqlArgsParameterHandler(mappedStatement, (Map) parameterObject, boundSql);
            return (ParameterHandler) interceptorChain.pluginAll(sqlArgsParameterHandler);
        } else {
            return super.newParameterHandler(mappedStatement, parameterObject, boundSql);
        }
    }
}
