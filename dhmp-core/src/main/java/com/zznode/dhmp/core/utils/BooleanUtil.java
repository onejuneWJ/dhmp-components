package com.zznode.dhmp.core.utils;

import java.util.Objects;

/**
 * Boolean类型相关工具类
 */
public class BooleanUtil extends cn.hutool.core.util.BooleanUtil {
    public static boolean isTrue(Integer flag) {
        return flag != null && Objects.equals(1, flag);
    }

    public static boolean isTrue(String flag) {

        return toBoolean(flag);
    }

    public static boolean isFalse(Integer flag) {
        return !isTrue(flag);
    }

    public static boolean isFalse(String flag) {
        return !isTrue(flag);
    }
}
