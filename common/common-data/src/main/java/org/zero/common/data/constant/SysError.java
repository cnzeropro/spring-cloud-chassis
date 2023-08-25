package org.zero.common.data.constant;

import lombok.AllArgsConstructor;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/12/1
 */
@AllArgsConstructor
public enum SysError implements BaseSysMessage {
    /**
     * 一切ok
     */
    OK("00000", "ok"),
    /**
     * 宏观错误
     */
    ERROR("11111", "error"),
    ;

    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
