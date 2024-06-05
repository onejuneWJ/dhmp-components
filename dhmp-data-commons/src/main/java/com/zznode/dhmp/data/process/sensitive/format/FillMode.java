package com.zznode.dhmp.data.process.sensitive.format;

/**
 * 屏蔽字符填充类型
 *
 * @author 王俊
 */
public enum FillMode {
    /**
     * 覆盖。将原有内容覆盖为新的长度的屏蔽字符
     */
    COVER,
    /**
     * 截断覆盖。将原有内容截断后，覆盖为新的长度的屏蔽字符
     */
    CUT_COVER,
    /**
     * 插入。在原位置插入定长的屏蔽字符，原内容往后移
     */
    INSERT,
    /**
     * 替换。原内容从开始位置每个字符依次替换为屏蔽字符
     */
    REPLACE
}
