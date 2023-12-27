package com.zznode.dhmp.lov.client;

import com.zznode.dhmp.lov.manager.LovManager;
import com.zznode.dhmp.lov.constant.LovConstants;
import com.zznode.dhmp.lov.domain.LovValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zznode.dhmp.lov.constant.LovConstants.LOV_CACHE_NAME;

/**
 * LovClient默认实现
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
public class DefaultLovClient implements LovClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 默认使用ConcurrentMapCache
     */
    private LovCache lovCache = new LovCache(new ConcurrentMapCache(LOV_CACHE_NAME));

    private LovManager lovManager;

    public void setLovCache(LovCache lovCache) {
        this.lovCache = lovCache;
    }

    public LovManager getLovManager() {
        return lovManager;
    }

    public void setLovManager(LovManager lovManager) {
        this.lovManager = lovManager;
    }

    @Override
    public String translate(String code, String value) {
        logger.trace("starting translate lov code: {} of value: {}", code, value);
        Map<String, String> map = lovCache.getLovValueCache(specifyCacheKey(code), () -> getFromDb(code));
        if (map == null || map.isEmpty()) {
            logger.error("cannot translate. cannot find lov of code: " + code);
            return null;
        }
        String translated = map.get(value);
        logger.trace("completed translate value: {}", translated);
        return translated;
    }

    private String specifyCacheKey(String code) {
        return LovConstants.LOV_CACHE_KEY + ":" + code;
    }

    protected Map<String, String> getFromDb(String lovCode) {
        Assert.notNull(lovManager, "no LovManager specified.");
        List<LovValue> lovValues = lovManager.getLovValues(lovCode);
        if (lovValues == null) {
            return Collections.emptyMap();
        }
        return lovValues.stream()
                .collect(Collectors.toMap(LovValue::getValue, LovValue::getName));
    }
}
