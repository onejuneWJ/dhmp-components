package com.zznode.dhmp.lov.domain;

import java.util.List;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
public class Lov {
    public static final String LOV_TABLE_NAME = "common_lov";

    private Long id;
    /**
     * 值集编码
     */
    private String lovCode;
    /**
     * 值集名称
     */
    private String lovName;
    /**
     * 描述
     */
    private String desc;


    private List<LovValue> lovValues;

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

    public String getLovName() {
        return lovName;
    }

    public void setLovName(String lovName) {
        this.lovName = lovName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<LovValue> getLovValues() {
        return lovValues;
    }

    public void setLovValues(List<LovValue> lovValues) {
        this.lovValues = lovValues;
    }
}
