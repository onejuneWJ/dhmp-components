package com.zznode.dhmp.export.converter;

import com.zznode.dhmp.lov.client.LovClient;

/**
 * lov转换
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
public class LovConverter {

    private LovClient lovClient;

    public void setLovClient(LovClient lovClient) {
        this.lovClient = lovClient;
    }

    public String convert(Object value, String lovCode) {
        return lovClient.translate(lovCode, String.valueOf(value));
    }

}
