package com.zznode.dhmp.core.strategy;

import java.util.function.Predicate;

/**
 * 策略工厂顶级接口
 *
 * @author 王俊
 * @date create in 2023/5/18 11:43
 */
public interface StrategyFactory<T extends Strategy> {
    /**
     * 给容器添加策略实现，可在运行时自行添加策略实现
     *
     * @param strategy 策略名称
     * @param instance 实例
     */
    default void addStrategy(String strategy, T instance) {
        addStrategy(strategy, instance, false);
    }

    /**
     * 给容器添加策略实现，策略实例化时会自动调用该方法
     *
     * @param strategy  策略名称
     * @param instance  策略实现
     * @param isDefault 是否做为默认策略
     */
    void addStrategy(String strategy, T instance, boolean isDefault);

    /**
     * 获取策略实例
     *
     * @param strategy 策略名称
     * @return 策略实例
     */
    T getStrategy(String strategy);

    /**
     * 获取名称满足predicate条件的策略实例
     *
     * @param predicate 满足
     * @return 策略实例
     */
    T getStrategy(Predicate<String> predicate);
}
