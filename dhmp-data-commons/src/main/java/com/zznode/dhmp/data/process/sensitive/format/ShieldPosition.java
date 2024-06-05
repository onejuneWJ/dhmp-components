package com.zznode.dhmp.data.process.sensitive.format;

/**
 * 屏蔽规则
 *
 * @author 王俊
 */
public enum ShieldPosition {
    /**
     * 屏蔽前面所有的字符串
     */
    BEFORE,
    /**
     * 屏蔽后面所有的字符串
     */
    AFTER,
    /**
     * 屏蔽自身
     */
    SELF
}
