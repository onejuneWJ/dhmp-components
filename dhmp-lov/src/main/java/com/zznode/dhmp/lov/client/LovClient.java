package com.zznode.dhmp.lov.client;

/**
 * 值集客户端
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
public interface LovClient {

    /**
     * 翻译值集
     * @param code 值集编码
     * @param value 值
     * @return 值集翻译后的值
     */
    String translate(String code, String value);

}
