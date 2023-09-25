package com.zznode.dhmp.lov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 值集缓存
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
public class LovCache implements InitializingBean {

    public static final String LOV_CACHE_NAME = "lovCache";

    private static final Logger logger = LoggerFactory.getLogger(LovCache.class);

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
