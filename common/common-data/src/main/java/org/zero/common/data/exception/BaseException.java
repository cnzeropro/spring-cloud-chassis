package org.zero.common.data.exception;

import org.zero.common.data.constant.BaseSysMessage;
import org.zero.common.data.constant.SysError;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2022/6/20
 */
public class BaseException extends RuntimeException {
    /**
     * 用户提示消息
     */
    protected final String promptMessage;
    /**
     * 系统错误信息
     */
    protected final BaseSysMessage sysMessage;

    public BaseException() {
        this((String) null);
    }

    public BaseException(String message) {
        this(message, message);
    }

    public BaseException(BaseSysMessage sysMessage) {
        this(sysMessage.getMessage(), sysMessage);
    }

    public BaseException(String message, String promptMessage) {
        this(message, promptMessage, SysError.ERROR);
    }

    public BaseException(String message, BaseSysMessage sysMessage) {
        this(message, message, sysMessage);
    }

    public BaseException(String message, String promptMessage, BaseSysMessage sysMessage) {
        super(message);
        this.promptMessage = promptMessage;
        this.sysMessage = sysMessage;
    }

    public BaseException(Throwable cause) {
        this(SysError.ERROR, cause);
    }

    public BaseException(String message, Throwable cause) {
        this(message, SysError.ERROR, cause);
    }

    public BaseException(BaseSysMessage sysMessage, Throwable cause) {
        this(sysMessage.getMessage(), sysMessage, cause);
    }

    public BaseException(String message, String promptMessage, Throwable cause) {
        this(message, promptMessage, SysError.ERROR, cause);
    }

    public BaseException(String message, BaseSysMessage sysMessage, Throwable cause) {
        this(message, message, sysMessage, cause);
    }

    public BaseException(String message, String promptMessage, BaseSysMessage sysMessage, Throwable cause) {
        super(message, cause);
        this.promptMessage = promptMessage;
        this.sysMessage = sysMessage;
    }

    protected BaseException(String message, String promptMessage, BaseSysMessage sysMessage, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.promptMessage = promptMessage;
        this.sysMessage = sysMessage;
    }

    public String getPromptMessage() {
        return promptMessage;
    }

    public BaseSysMessage getSysMessage() {
        return sysMessage;
    }

    public String getErrorCode() {
        return sysMessage.getCode();
    }

    public String getErrorMessage() {
        return sysMessage.getMessage();
    }
}
