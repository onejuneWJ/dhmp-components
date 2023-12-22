package com.zznode.dhmp.lov.domain;

/**
 * 表：common_lov_values
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
public class LovValue {

    public static final String LOV_VALUE_TABLE_NAME = "common_lov_value";

    private Long id;
    /**
     * 值集编码
     */
    private String lovCode;
    /**
     * 页面展示值
     */
    private String name;
    /**
     * 值
     * 值全部转为string类型
     */
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLovCode() {
        return lovCode;
    }

    public void setLovCode(String lovCode) {
        this.lovCode = lovCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
