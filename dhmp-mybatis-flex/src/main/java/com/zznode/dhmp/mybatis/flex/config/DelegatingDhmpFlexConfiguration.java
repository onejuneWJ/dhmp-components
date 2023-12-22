package com.zznode.dhmp.mybatis.flex.config;

import com.mybatisflex.annotation.SetListener;
import com.zznode.dhmp.mybatis.flex.config.customizer.DhmpMybatisFlexCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 描述
 *
 * @author 王俊
 */
@Configuration(proxyBeanMethods = false)
public class DelegatingDhmpFlexConfiguration extends DhmpFlexConfigurationSupport {

    DhmpFlexConfigurerComposite configurers = new DhmpFlexConfigurerComposite();

    @Autowired(required = false)
    public void setConfigurers(List<DhmpFlexConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addConfigurers(configurers);
        }
    }

    @Override
    protected void addSetListeners(List<SetListener> setListeners) {
        this.configurers.addSetListeners(setListeners);
    }

    @Override
    protected void addCustomizers(List<DhmpMybatisFlexCustomizer> customizers) {
       this.configurers.addCustomizers(customizers);
    }
}
