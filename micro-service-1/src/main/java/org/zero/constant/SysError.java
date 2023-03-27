package org.zero.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/12/1
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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
