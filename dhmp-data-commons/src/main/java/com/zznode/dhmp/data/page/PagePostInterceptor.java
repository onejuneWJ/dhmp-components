package com.zznode.dhmp.data.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 分页拦截器执行之后的拦截器。
 * <p>在PageInterceptor拦截并设置完分页sql后，清空缓存的page，待sql执行完成之后把page设置回来。
 * <p>本拦截器避免了在执行查询过程中,嵌套查询会再次被PageInterceptor拦截从而执行为分页查询,导致查询结果出错。
 * (如lov翻译时：在查询对象后ResultSetHandler处理值的时候会对有@LovValue的字段进行值集翻译,如果这个值集code没有在缓存中,
 * 可能会进行数据库查询值集的值列表,这时如果没有本拦截器处理,会导致返回的结果集为查询到的值集列表,而不是原来的查询结果集)
 *
 * @author 王俊
 * @see com.github.pagehelper.PageInterceptor
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class PagePostInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if (args.length == 4) {
            //4 个参数时
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }

        // 查看是否需要分页查询。
        // PageInterceptor在判断是否存需要分页后,如果需要分页,PageHelper.getLocalPage()都不会为空
        Page page = PageHelper.getLocalPage();
        if (page != null) {
            // 暂时清空分页缓存
            PageHelper.clearPage();
            try {
                return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            } finally {
                //执行完sql之后再缓存回去。
                PageHelper.setLocalPage(page);
            }
        }
        return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
    }


}
