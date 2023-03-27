package org.zero.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.zero.constant.SysError;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 7893804841950761019L;

    public static final String OK_MSG = "操作成功";
    public static final String ERROR_MSG = "操作失败";
    public static final String FAIL_MSG = "请求错误";

    /**
     * HTTP 状态码
     */
    private int code;
    /**
     * 用户提示信息
     */
    private String msg;
    /**
     * 错误码
     */
    private SysError error;

    /**
     * 成功标志
     */
    private boolean success;
    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();

    /**
     * 数据对象
     */
    private T data;

    /* ******************************************************** 请求成功 ******************************************************** */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return ok(OK_MSG, data);
    }

    public static <T> Result<T> ok(String msg) {
        return ok(msg, null);
    }

    public static <T> Result<T> ok(String msg, T data) {
        return of(HttpStatus.OK, msg, SysError.OK, data);
    }

    /* ******************************************************** 请求成功但不是预期响应 ******************************************************** */
    public static <T> Result<T> error() {
        return error(ERROR_MSG);
    }

    public static <T> Result<T> error(String msg) {
        return error(msg, SysError.ERROR);
    }

    public static <T> Result<T> error(T data) {
        return error(ERROR_MSG, data);
    }

    public static <T> Result<T> error(String msg, T data) {
        return of(HttpStatus.OK, msg, SysError.ERROR, data);
    }

    public static <T> Result<T> error(String msg, SysError error) {
        return error(msg, error, null);
    }

    public static <T> Result<T> error(String msg, SysError error, T data) {
        return of(HttpStatus.OK, msg, error, data);
    }

    /* ******************************************************** 请求失败 ******************************************************** */
    public static <T> Result<T> fail() {
        return fail(FAIL_MSG);
    }

    public static <T> Result<T> fail(String msg) {
        return fail(msg, SysError.ERROR);
    }

    public static <T> Result<T> fail(String msg, SysError error) {
        return fail(HttpStatus.INTERNAL_SERVER_ERROR, msg, error);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return fail(code, msg, SysError.ERROR);
    }

    public static <T> Result<T> fail(HttpStatus code, String msg) {
        return fail(code, msg, SysError.ERROR);
    }

    public static <T> Result<T> fail(HttpStatus httpStatus, String msg, SysError error) {
        return of(httpStatus, msg, error, null);
    }

    public static <T> Result<T> fail(int code, String msg, SysError error) {
        return of(code, msg, error, null);
    }

    /* ******************************************************** 通用构造 ******************************************************** */

    public static <T> Result<T> of(HttpStatus httpStatus, String msg, SysError error, T data) {
        return of(httpStatus.value(), msg, error, LocalDateTime.now(), data);
    }

    public static <T> Result<T> of(int code, String msg, SysError error, T data) {
        return of(code, msg, error, LocalDateTime.now(), data);
    }

    public static <T> Result<T> of(int code, String msg, SysError error, LocalDateTime time, T data) {
        if (Objects.isNull(error)) {
            return of(code, msg, null, HttpStatus.OK.value() == code, time, data);
        }

        return of(code, msg, error, HttpStatus.OK.value() == code && SysError.OK.getCode().equals(error.getCode()), time, data);
    }

    public static <T> Result<T> of(int code, String msg, SysError error, boolean success, LocalDateTime time, T data) {
        return Result.<T>builder()
                .code(code)
                .msg(msg)
                .error(error)
                .success(success)
                .time(time)
                .data(data)
                .build();
    }
}
