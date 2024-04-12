package com.zznode.dhmp.core.constant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * 省份
 * <p>
 * 省份 区号 北京 010 天津 022 上海 021 重庆 023 河北 0311、0313 山西 0351、0352 辽宁 024、0411、0421 吉林 0431、0432 黑龙江 0451、0452 内蒙古 0471、0472、0473、0474 河南 0371、0391 山东 0531、0631 江苏 025、0511、0512 浙江 0571、0572 安徽 0551、0552 福建 0591、0592 江西 0791、0792 湖北 027、0711 湖南 0731、0732 广东 020、0755、0756、0754、0751、0752、0769、0760 广西 0771、0772 海南 0898 重庆 023 四川 028、0816、0831 贵州 0851、0852 云南 0871、0872 西藏 0891、0892 陕西 029、0911 甘肃 0931、0932 青海 0971、0972 宁夏 0951、0952 新疆 0991、0992、0993、0994
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

    private static final Log logger = LogFactory.getLog(Province.class);

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
