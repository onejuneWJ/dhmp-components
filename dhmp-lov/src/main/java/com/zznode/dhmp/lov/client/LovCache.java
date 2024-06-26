package com.zznode.dhmp.lov.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.zznode.dhmp.lov.constant.LovConstants.LOV_CACHE_NAME;

/**
 * 值集缓存
 * <p>建议使用ConcurrentMapCache。
 * <p>经过测试，在查询几万条数据时,使用redis性能不够好。redis不适合这种场景
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
public class LovCache implements InitializingBean {

    private static final Log logger = LogFactory.getLog(LovCache.class);

    private final Cache cache;

    public LovCache(Cache cache) {
        Assert.notNull(cache, "cache cannot be null");
        this.cache = cache;
    }

    public LovCache(CacheManager cacheManager) {
        Assert.notNull(cacheManager, "cacheManager cannot be null");
        Cache cache = cacheManager.getCache(LOV_CACHE_NAME);
        if(cache == null) {
            logger.warn("cannot get cache for name " + LOV_CACHE_NAME + "from cache manager." +
                    "using map cache");
            cache = new ConcurrentMapCache(LOV_CACHE_NAME);
        }
        this.cache = cache;
    }

    public Map<String, String> getLovValueCache(String lovCode) {
        return getLovValueCache(lovCode, null);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getLovValueCache(String lovCode, Callable<Map<String, String>> valueLoader) {
        if (valueLoader == null) {
            return cache.get(lovCode, Map.class);
        }
        return cache.get(lovCode, valueLoader);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
