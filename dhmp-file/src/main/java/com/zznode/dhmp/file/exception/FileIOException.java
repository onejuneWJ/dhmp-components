package com.zznode.dhmp.file.exception;

import java.io.IOException;

/**
 * 封装一层，用于跟IOException区分
 *
 * @author 王俊
 */
public class FileIOException extends IOException {
    public FileIOException(String message) {
        super(message);
    }

    public FileIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileIOException(Throwable cause) {
        super(cause);
    }
}
