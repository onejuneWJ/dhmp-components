package com.zznode.dhmp.mybatis.flex.config;

import com.mybatisflex.annotation.SetListener;
import com.zznode.dhmp.mybatis.flex.config.customizer.DhmpMybatisFlexCustomizer;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述
 *
 * @author 王俊
 */
public class DhmpFlexConfigurerComposite implements DhmpFlexConfigurer {

    private final List<DhmpFlexConfigurer> delegates = new ArrayList<>();

    public void addConfigurers(List<DhmpFlexConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.delegates.addAll(configurers);
        }
    }


    @Override
    public void addSetListeners(List<SetListener> setListeners) {
        for (DhmpFlexConfigurer delegate : this.delegates) {
            delegate.addSetListeners(setListeners);
        }
    }

    @Override
    public void addCustomizers(List<DhmpMybatisFlexCustomizer> customizerList) {
        for (DhmpFlexConfigurer delegate : this.delegates) {
            delegate.addCustomizers(customizerList);
        }
    }
}
