package com.zznode.dhmp.data.process.sensitive.interceptor;

import com.zznode.dhmp.data.process.sensitive.SensitiveDataProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.function.SingletonSupplier;

import java.util.function.Supplier;

/**
 * 脱敏切面支持
 *
 * @author 王俊
 */
public abstract class SensitiveAspectSupport {


    protected final Log log = LogFactory.getLog(getClass());

    /**
     * 使用懒加载模式
     */
    private Supplier<SensitiveDataProcessor> defaultProcessor;

    public SensitiveAspectSupport() {
        this(null);
    }

    public SensitiveAspectSupport(@Nullable SensitiveDataProcessor dataProcessor) {
        this.defaultProcessor = new SingletonSupplier<>(dataProcessor, SensitiveDataProcessor::new);
    }

    public void configure(@Nullable Supplier<SensitiveDataProcessor> dataProcessor) {
        this.defaultProcessor = new SingletonSupplier<>(dataProcessor, SensitiveDataProcessor::new);
    }

    public void sensitiveData(@Nullable Object data) {
        getDataProcessor().process(data);
    }

    protected SensitiveDataProcessor getDataProcessor() {
        SensitiveDataProcessor processor = this.defaultProcessor.get();
        if (processor == null) {
            throw new IllegalStateException(
                    "No SensitiveDataProcessor specified and no default processor set either");
        }
        return processor;
    }
}
