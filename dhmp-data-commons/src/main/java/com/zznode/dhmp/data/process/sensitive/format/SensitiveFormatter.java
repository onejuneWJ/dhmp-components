package com.zznode.dhmp.data.process.sensitive.format;

import com.zznode.dhmp.data.process.sensitive.format.SensitiveFormatterBuilder.*;

/**
 * 脱敏格式化
 *
 *
 * @author 王俊
 */
public final class SensitiveFormatter {


    /**
     * 中文名字，只显示第一个汉字，其他隐藏为2个星号，比如：王**
     */
    public static final String CHINESE_NAME = "F{3}&#{0,1}";

    public static final SensitiveFormatter CHINESE_NAME_FORMATTER;

    static {
        CHINESE_NAME_FORMATTER = builder()
                .shieldRange(1, 2, FillMode.CUT_COVER)
                .build();
    }

    /**
     * 身份证号，显示最后四位，其他隐藏。共计18位或者15位，比如：*************1234
     */
    public static final String ID_CARD = "F{0}&#{-4,4}";

    public static final SensitiveFormatter ID_CARD_FORMATTER = builder()
            .shieldAll()
            .unShield(-4, 4)
            .build();
    /**
     * 固定电话，显示后4位，其他隐藏，比如：****1234
     */
    public static final String FIXED_PHONE = "F{0}&#{-4,4}";

    public static final SensitiveFormatter FIXED_PHONE_FORMATTER = ID_CARD_FORMATTER;
    /**
     * 手机号码，前三位，后四位，其他隐藏，比如135****1111
     */
    public static final String MOBILE_PHONE = "{3,-1}&#{-4,4}";

    public static final SensitiveFormatter MOBILE_PHONE_FORMATTER = builder()
            .shieldRange(3, -1)
            .unShield(-4, 4)
            .build();

    /**
     * 密码。替换为6个*，比如：******
     */
    public static final String PASSWORD = "F{6}";

    public static final SensitiveFormatter PASSWORD_FORMATTER = builder()
            .fixedLength(6)
            .build();
    /**
     * 账号，替换为8个*
     */
    public static final String ACCOUNT = "F{8}";

    public static final SensitiveFormatter ACCOUNT_FORMATTER = builder()
            .fixedLength(8)
            .build();


    /**
     * 邮箱，将@前的邮箱前缀替换成4个*，比如：abcde@qq.com -> ****@qq.com
     * <p>
     */
    public static final String EMAIL = "S{[@]B}&#{0,2}";

    public static final SensitiveFormatter EMAIL_FORMATTER = builder()
            .shieldTextPosition("@", ShieldPosition.BEFORE, 4)
            .unShield(0, 2)
            .build();

    /**
     * 构造SensitiveFormatterBuilder实例
     *
     * @return 构造器实例
     */
    public static SensitiveFormatterBuilder builder() {
        return new SensitiveFormatterBuilder();
    }

    /**
     * 根据传入的pattern构造SensitiveFormatter实例。
     * <p>
     * 请尽量使用builder()，因为pattern太局限
     *
     * @param pattern 格式
     * @return SensitiveFormatter
     */
    public static SensitiveFormatter ofPattern(String pattern) {
        if (pattern.isEmpty()) {
            return noOpFormatter;
        }
        return builder().appendPattern(pattern).build();
    }

    /**
     * 什么也不做，返回原来的
     */
    private static final SensitiveFormatter noOpFormatter;

    static {
        noOpFormatter = builder().build();
    }


    private final CompositePrinterParser parser;

    /**
     * 构造SensitiveFormatter实例
     *
     * @param parser 解析器
     */
    SensitiveFormatter(CompositePrinterParser parser) {
        this.parser = parser;
    }

    public String format(String str) {

        StringBuilder sb = new StringBuilder(str);
        formatTo(str, sb);
        return sb.toString();
    }

    private void formatTo(String str, StringBuilder sb) {
        this.parser.format(str, sb);
    }

}
