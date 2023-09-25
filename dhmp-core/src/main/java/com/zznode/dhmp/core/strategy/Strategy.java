package com.zznode.dhmp.core.strategy;

import jakarta.annotation.PostConstruct;

/**
 * 策略模式-策略顶级类
 * <p>策略接口继承该类，并默认实现initialize()方法
 * <p>策略实现类实现getStrategyName()方法
 * <p>策略实现了需要使用@ProvinceComponent注解标记,以便不同省份实现不同逻辑
 *
 * @author 王俊
 * @date create in 2023/5/17 13:48
 * @see AbstractStrategyFactory
 */
public interface Strategy {
    /**
     * 初始化方法，在实例新建时将该策略实例添加至工厂中
     * <p>例子:
     * <pre>
     * public interface OneStrategy extends Strategy {
     *
     *  void test();
     *
     *  &#64;PostConstruct
     *  &#64;Override
     *  default void initialize(){
     *      OneStrategyFactory.getInstance().addStrategy(getStrategyName(),this);
     *  }
     *
     * }
     * </pre>
     * 注意：此方法中@PostConstruct毫无作用，将该方法定义在顶级类是为了提醒子类必须实现该方法(并且实现方法需要添加@PostConstruct注解)
     *
     * @see AbstractStrategyFactory#addStrategy(String, Strategy, boolean)
     */
    @PostConstruct
    void initialize();

    /**
     * 定义策略实例的名称
     *
     * @return 策略名称
     */
    String getStrategyName();

    /**
     * 是否做为默认策略,默认为false。
     * <p>如需使当前策略实现作为默认策略实现，则实现重写该方法，返回true
     *
     * @return true if the strategy is default
     */
    default boolean isDefault() {
        return false;
    }
}
