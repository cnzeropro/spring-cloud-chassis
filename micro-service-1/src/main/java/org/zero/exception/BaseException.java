package org.zero.exception;

import lombok.Getter;
import org.zero.constant.SysError;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/6/20
 */
@Getter
public class BaseException extends RuntimeException {
    private final SysError sysError;

    public BaseException() {
        this(SysError.ERROR);
    }

    public BaseException(SysError sysError) {
        this(sysError.getMsg(), sysError);
    }

    public BaseException(String message) {
        this(message, SysError.ERROR);
    }

    public BaseException(String message, SysError sysError) {
        super(message);
        this.sysError = sysError;
    }

    public BaseException(String message, Throwable cause) {
        this(message, SysError.ERROR, cause);
    }

    public BaseException(SysError sysError, Throwable cause) {
        this(sysError.getMsg(), sysError, cause);
    }

    public BaseException(String message, SysError sysError, Throwable cause) {
        super(message, cause);
        this.sysError = sysError;
    }

    public BaseException(String message, SysError sysError, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.sysError = sysError;
    }

    public String getErrorCode() {
        return sysError.getCode();
    }

    public String getErrorMsg() {
        return sysError.getMsg();
    }
}
