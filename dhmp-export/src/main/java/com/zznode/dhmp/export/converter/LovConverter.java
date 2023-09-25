package com.zznode.dhmp.export.converter;

import com.zznode.dhmp.lov.client.LovClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * lov转换
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
public class LovConverter {

    private final Logger logger = LoggerFactory.getLogger(LovConverter.class);

    private LovClient lovClient;

    public void setLovClient(LovClient lovClient) {
        this.lovClient = lovClient;
    }

    public String convert(Object value, String lovCode) {
        return lovClient.translate(lovCode, String.valueOf(value));
    }

}
