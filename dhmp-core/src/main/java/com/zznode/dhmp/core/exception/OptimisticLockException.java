package com.zznode.dhmp.core.exception;

import com.zznode.dhmp.core.constant.BaseConstants;

/**
 * 描述
 *
 * @author 王俊
 * @date create in 2023/6/28 10:48
 */
public class OptimisticLockException extends RuntimeException {

    public OptimisticLockException() {
        super(BaseConstants.ErrorCode.OPTIMISTIC_LOCK);
    }

}
