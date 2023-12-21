package com.zznode.dhmp.mybatis.flex.config;

import com.mybatisflex.annotation.SetListener;
import com.zznode.dhmp.mybatis.flex.config.customizer.DhmpMybatisFlexCustomizer;
import com.zznode.dhmp.mybatis.flex.listener.set.DelegatingSetListener;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * mybati-flex配置支持
 *
 * @author 王俊
 */

public class DhmpFlexConfigurationSupport {

    @Nullable
    private List<SetListener> setListeners;

    @Bean
    DelegatingSetListener setListenerComposite() {
        DelegatingSetListener setListener = new DelegatingSetListener();
        setListener.setListeners(getSetListeners());
        return setListener;
    }

    protected final List<SetListener> getSetListeners() {
        if (this.setListeners == null) {
            this.setListeners = new ArrayList<>();
            addSetListeners(this.setListeners);
        }
        return this.setListeners;
    }

    protected void addSetListeners(List<SetListener> setListeners) {

    }

    protected void addCustomizers(List<DhmpMybatisFlexCustomizer> customizers) {

    }
}
