package com.zznode.dhmp.export;

import java.io.Serial;

/**
 * 导出数据过程中发生的异常
 *
 * @author 王俊
 */
public class ExportException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8025127522930916810L;

    public ExportException(String message) {
        super(message);
    }
}
