package com.zznode.dhmp.lov.manager;

import com.zznode.dhmp.lov.domain.LovValue;

import java.util.List;

/**
 * 值集管理
 *
 * @author 王俊
 * @date create in 2023/8/31
 */
public interface LovManager {

    /**
     * 根据值集编码获取值集值列表
     *
     * @param lovCode 值集编码
     * @return 值集值列表
     */
    List<LovValue> getLovValues(String lovCode);
}
