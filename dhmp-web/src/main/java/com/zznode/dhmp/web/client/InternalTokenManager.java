package com.zznode.dhmp.web.client;

/**
 * 内部调用token管理
 *
 * @author 王俊
 * @date create in 2023/8/25
 */
public interface InternalTokenManager {

    /**
     * 生成token
     * @return TOKEN
     */
    String generateToken();

    /**
     * 校验token
     * @param token token
     * @return token合法
     */
    boolean validate(String token);

}
