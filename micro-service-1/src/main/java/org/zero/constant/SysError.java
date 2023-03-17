package org.zero.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/12/1
 */
@AllArgsConstructor
@Getter
public enum SysError {
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
    private final String msg;
}
