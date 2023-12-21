package com.zznode.dhmp.mybatis.flex.listener.set;

import com.mybatisflex.annotation.SetListener;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * 描述
 *
 * @author 王俊
 */
public class DelegatingSetListener implements SetListener {
    @Nullable
    private List<SetListener> listeners;

    public void setListeners(@Nullable List<SetListener> listeners) {
        this.listeners = listeners;
    }

    public List<SetListener> getListeners() {
        return (this.listeners != null ? Collections.unmodifiableList(this.listeners) : Collections.emptyList());
    }

    /**
     * 实体类属性设置。
     *
     * @param entity   实体类
     * @param property 属性名
     * @param value    属性值
     * @return 实体类
     */
    @Override
    public Object onSet(Object entity, String property, Object value) {
        List<SetListener> setListenerList = getListeners();
        for (SetListener setListener : setListenerList) {
            if (setListener instanceof SupportedListener supportedListener) {
                if (supportedListener.supports(entity, property, value)) {
                    value = supportedListener.onSet(entity, property, value);
                }
            } else {
                value = setListener.onSet(entity, property, value);
            }
        }
        return value;
    }

    /**
     * <p>多个监听器时的执行顺序。
     *
     * <p>值越小越早触发执行。
     *
     * @return order
     */
    @Override
    public int order() {
        return SetListener.super.order();
    }
}
