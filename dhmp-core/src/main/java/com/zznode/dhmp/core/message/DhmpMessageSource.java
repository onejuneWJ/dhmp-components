package com.zznode.dhmp.core.message;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/6/28 11:37
 */
public class DhmpMessageSource extends ResourceBundleMessageSource {

    private static final DhmpMessageSource INSTANCE = new DhmpMessageSource();

    public static DhmpMessageSource getInstance() {
        return INSTANCE;
    }

    private DhmpMessageSource() {
        setBasenames("messages.messages_core", "messages.messages");
        setDefaultEncoding("UTF-8");
        setUseCodeAsDefaultMessage(true);
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(INSTANCE);
    }

    public static void addBasename(String basename) {
        INSTANCE.addBasenames(basename);
    }
}
