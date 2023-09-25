package com.zznode.dhmp.core.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * 省份
 *
 * @author 王俊
 * @date create in 2023/5/12 9:50
 */
public enum Province implements Serializable {

    /**
     * 全国
     */


    UNKNOWN("0", "未知"),

    BEI_JING("010", "北京"),

    SHANG_HAI("021", "上海"),

    HE_BEI("031", "河北"),

    LIAO_NING("041", "辽宁"),

    JI_LIN("043", "吉林"),

    NEI_MENG_GU("047", "内蒙古"),

    SHAN_DONG("053", "山东"),

    HU_BEI("071", "湖北"),

    HU_NAN("073", "湖南"),

    NING_XIA("095", "宁夏");

    private static final Logger logger = LoggerFactory.getLogger(Province.class);

    /**
     * 当前生效省份<p>
     */
    private static Province CURRENT_PROVINCE = UNKNOWN;

    private final String provinceCode;
    private final String provinceName;

    Province(String provinceCode, String provinceName) {
        Assert.hasText(provinceCode, "provinceCode cannot be empty");
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
    }


    public String getProvinceCode() {
        return this.provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    @NonNull
    public static Province currentProvince() {
        return CURRENT_PROVINCE;
    }

    public static String currentProvinceName() {
        return currentProvince().getProvinceCode();
    }

    public static String currentProvinceCode() {
        return currentProvince().getProvinceCode();
    }

    private static boolean initialized = false;

    /**
     * 设置当前省份
     *
     * @param province 省份
     * @see com.zznode.core.context.DhmpApplicationContextInitializer DhmpApplicationContextInitializer
     */
    public static void setCurrentProvince(Province province) {
        Assert.notNull(province, "Province cannot be null. please provide a Province on properties file!");
        if (!isConcreteProvince(province)) {
            throw new IllegalArgumentException("must provide a concrete province. ");
        }
        // 保证此方法只调用一次
        if (!initialized) {
            CURRENT_PROVINCE = province;
            initialized = true;
            return;
        }
        logger.warn("can not change current provinces after application initialized");
    }

    public static Province fromProvinceCode(String provinceCode) {
        for (Province value : values()) {
            if (value.getProvinceCode().equals(provinceCode)) {
                return value;
            }
        }
        logger.warn("unrecognized provinces code: " + provinceCode);
        return UNKNOWN;
    }

    public static boolean isConcreteProvince(Province province) {
        return province != null && !province.equals(UNKNOWN);
    }
}
