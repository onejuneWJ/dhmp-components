package com.zznode.dhmp.core.strategy;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 策略工厂抽象类
 *
 * @author 王俊
 * @date create in 2023/5/17 13:49
 */
public abstract class AbstractStrategyFactory<T extends Strategy> implements StrategyFactory<T> {

    @Override
    public void addStrategy(String strategy, T instance, boolean isDefault) {
        Assert.notNull(strategy, "strategyName cannot be null");
        strategiesContainer().put(strategy, instance);
        if (isDefault) {
            setDefaultStrategy(instance);
        }
    }

    @Override
    public T getStrategy(String strategy) {
        T instance = strategiesContainer().get(strategy);
        return instance != null ? instance : Objects.requireNonNull(getDefaultStrategy());
    }

    @Override
    public T getStrategy(Predicate<String> predicate) {
        if (predicate != null) {
            Set<Map.Entry<String, T>> entrySet = strategiesContainer().entrySet();
            for (Map.Entry<String, T> entry : entrySet) {
                if (predicate.test(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return Objects.requireNonNull(getDefaultStrategy());
    }

    /**
     * 获取实例储存容器
     *
     * @return 子类实现
     */
    protected abstract Map<String, T> strategiesContainer();

    /**
     * 默认策略实现，
     *
     * @return 子类实现
     */
    @Nullable
    protected abstract T getDefaultStrategy();

    /**
     * 默认策略实现，子类实现
     *
     * @param defaultStrategy 默认策略实现
     */
    protected abstract void setDefaultStrategy(T defaultStrategy);
}
