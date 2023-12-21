package com.zznode.dhmp.mybatis.flex.listener.set;

import cn.hutool.core.util.ReflectUtil;
import com.mybatisflex.annotation.SetListener;
import com.mybatisflex.core.util.Reflectors;
import com.zznode.dhmp.lov.annotation.LovValue;
import com.zznode.dhmp.lov.client.LovClient;
import org.apache.ibatis.reflection.Reflector;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * LOV转换
 *
 * @author 王俊
 */
public class LovSetListener implements SupportedListener, SetListener {

    private final ObjectProvider<LovClient> lovClientObjectProvider;

    private LovClient lovClient;

    public LovSetListener(ObjectProvider<LovClient> lovClientObjectProvider) {
        this.lovClientObjectProvider = lovClientObjectProvider;
    }

    /**
     * 实体类属性设置。
     *
     * @param entity   实体类
     * @param property 属性名
     * @param value    属性值
     * @return 值
     */
    @Override
    public Object onSet(Object entity, String property, Object value) {

        if(this.lovClient == null){
            this.lovClient = this.lovClientObjectProvider.getIfAvailable();
        }
        Assert.notNull(lovClient, "lovClient cannot be null");

        Reflector reflector = Reflectors.of(entity.getClass());
        Field field = ReflectUtil.getField(entity.getClass(), property);
        LovValue annotation = AnnotationUtils.findAnnotation(field, LovValue.class);
        if (annotation != null) {
            // 目标字段
            String targetField;
            if (StringUtils.hasText(annotation.titleField())) {
                targetField = annotation.titleField();
            } else {
                // 如果没有填写目标字段,则跳过
                return value;
            }
            // 翻译
            String translate = lovClient.translate(annotation.value(), String.valueOf(value));
            try {
                reflector.getSetInvoker(targetField).invoke(entity, new String[]{translate});
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return value;
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
    public boolean supports(Object entity, String property, Object value) {
        Field field = ReflectUtil.getField(entity.getClass(), property);
        LovValue annotation = AnnotationUtils.findAnnotation(field, LovValue.class);
        return annotation != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LovSetListener that = (LovSetListener) o;
        return Objects.equals(lovClient, that.lovClient);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
