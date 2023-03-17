package org.zero.assembly.spring.boot.handler;

import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zero.exception.BaseException;
import org.zero.exception.UtilException;
import org.zero.model.vo.Result;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 异常处理器
 * 异常建议从小到大
 *
 * @author Zero
 * @date 2020/03/21
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /* *************************************************** 系统自定义异常 *************************************************** */

    @ExceptionHandler(UtilException.class)
    public Result<Void> utilException(UtilException e) {
        log.error("Util class method call exception", e);
        return Result.fail("系统错误，请联系管理员");
    }

    @ExceptionHandler(BaseException.class)
    public Result<Void> baseException(BaseException e) {
        log.error("System macro exception", e);
        return Result.fail(e.getMessage(), e.getSysError());
    }

    /* *************************************************** web异常 *************************************************** */

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public Result<Void> noHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException e) {
        log.error(StrUtil.format("The request did not find the resource: {} ({})", e.getRequestURL(), e.getHttpMethod()), e);
        return Result.fail(HttpStatus.NOT_FOUND, "请求资源未找到，请检查URL");
    }

    @ExceptionHandler(org.springframework.web.servlet.ModelAndViewDefiningException.class)
    public Result<Void> modelAndViewDefiningException(org.springframework.web.servlet.ModelAndViewDefiningException e) {
        log.error("Model and view definition exception", e);
        return Result.fail("模型、视图定义错误");
    }

    @ExceptionHandler(org.springframework.web.bind.MissingRequestHeaderException.class)
    public Result<Void> missingRequestHeaderException(org.springframework.web.bind.MissingRequestHeaderException e) {
        log.error(StrUtil.format("Missing request header: {} ({})", e.getHeaderName(), e.getParameter()), e);
        return Result.fail(StrUtil.format("请求头缺失：{}", e.getHeaderName()));
    }

    @ExceptionHandler(org.springframework.web.bind.MissingRequestCookieException.class)
    public Result<Void> missingRequestCookieException(org.springframework.web.bind.MissingRequestCookieException e) {
        log.error(StrUtil.format("Missing cookie: {} ({})", e.getCookieName(), e.getParameter()), e);
        return Result.fail(StrUtil.format("Cookie缺失：{}", e.getCookieName()));
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public Result<Void> missingServletRequestParameterException(org.springframework.web.bind.MissingServletRequestParameterException e) {
        log.error(StrUtil.format("Missing request parameter: {} ({})", e.getParameterName(), e.getParameterType()), e);
        return Result.fail(StrUtil.format("请求参数缺失：{}", e.getParameterName()));
    }

    @ExceptionHandler(org.springframework.web.bind.MissingPathVariableException.class)
    public Result<Void> missingPathVariableException(org.springframework.web.bind.MissingPathVariableException e) {
        log.error(StrUtil.format("Missing path parameter: {} ({})", e.getVariableName(), e.getParameter()), e);
        return Result.fail(StrUtil.format("路径参数缺失：{}", e.getVariableName()));
    }

    @ExceptionHandler(org.springframework.web.bind.MissingMatrixVariableException.class)
    public Result<Void> missingMatrixVariableException(org.springframework.web.bind.MissingMatrixVariableException e) {
        log.error(StrUtil.format("Missing matrix parameter: {} ({})", e.getVariableName(), e.getParameter()), e);
        return Result.fail(StrUtil.format("矩阵参数缺失：{}", e.getVariableName()));
    }

    @ExceptionHandler(org.springframework.web.bind.MissingRequestValueException.class)
    public Result<Void> missingRequestValueException(org.springframework.web.bind.MissingRequestValueException e) {
        log.error("Missing request value", e);
        return Result.fail("缺失请求值");
    }

    @ExceptionHandler(org.springframework.web.multipart.support.MissingServletRequestPartException.class)
    public Result<Void> missingServletRequestPartException(org.springframework.web.multipart.support.MissingServletRequestPartException e) {
        log.error(StrUtil.format("Missing [multipart/form-data] parameter: {}", e.getRequestPartName()), e);
        return Result.fail(StrUtil.format("[multipart/form-data]类型参数缺失：{}", e.getRequestPartName()));
    }

    @ExceptionHandler(org.springframework.web.HttpSessionRequiredException.class)
    public Result<Void> httpSessionRequiredException(org.springframework.web.HttpSessionRequiredException e) {
        log.error(StrUtil.format("Http session expected: {}", e.getExpectedAttribute()), e);
        return Result.fail(StrUtil.format("Session不存在：{}", e.getExpectedAttribute()));
    }

    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public Result<Void> httpMediaTypeNotSupportedException(org.springframework.web.HttpMediaTypeNotSupportedException e) {
        log.error(StrUtil.format("The media type[{}] is not supported, only supported: {}", e.getContentType(), e.getSupportedMediaTypes()), e);
        return Result.fail(StrUtil.format("媒体类型（MediaType）不支持：{}", e.getContentType()));
    }

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public Result<Void> httpRequestMethodNotSupportedException(org.springframework.web.HttpRequestMethodNotSupportedException e) {
        log.error(StrUtil.format("The request method[{}] is not supported, only supported: {}", e.getMethod(), e.getSupportedMethods()), e);
        return Result.fail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, StrUtil.format("请求方法不支持：{}", e.getMethod()));
    }

    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public Result<Void> maxUploadSizeExceededException(org.springframework.web.multipart.MaxUploadSizeExceededException e) {
        String size = DataSizeUtil.format(e.getMaxUploadSize());
        log.error(StrUtil.format("The uploaded file exceeds the specified size: {}", size), e);
        return Result.fail(StrUtil.format("上传文件超出指定大小[{}]，请压缩或降低文件质量", size));
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public Result<Void> constraintViolationException(javax.validation.ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        log.error(StrUtil.format("Parameter validation conflicts: {}", messages), e);
        return Result.fail("参数效验冲突");
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public Result<Void> methodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException e) {
        String msg = Optional.of(e.getBindingResult())
                .map(BindingResult::getAllErrors)
                .map(errors -> errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ", "[", "]")))
                .orElse("[]");
        log.error(StrUtil.format("The request parameter validation is abnormal: {}", msg), e);
        return Result.fail(HttpStatus.BAD_REQUEST, msg.replace(", ", "；"));
    }

    @ExceptionHandler(org.springframework.validation.BindException.class)
    public Result<Void> bindException(org.springframework.validation.BindException e) {
        List<ObjectError> errors = Optional.of(e.getBindingResult()).map(BindingResult::getAllErrors).orElse(Collections.emptyList());
        log.error(StrUtil.format("Data binding exception: {}", errors), e);
        return Result.fail(StrUtil.format("数据绑定错误：{}", errors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("；"))));
    }

    /* *************************************************** SQL异常 *************************************************** */

    @ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
    public Result<Void> sqlIntegrityConstraintViolationException(java.sql.SQLIntegrityConstraintViolationException e) {
        log.error("SQL integrity constraint violation", e);
        return Result.fail("已存在该数据");
    }

    @ExceptionHandler(java.sql.BatchUpdateException.class)
    public Result<Void> batchUpdateException(java.sql.BatchUpdateException e) {
        log.error("Batch update exception", e);
        return Result.fail("批量更新异常");
    }

    @ExceptionHandler(java.sql.DataTruncation.class)
    public Result<Void> dataTruncation(java.sql.DataTruncation e) {
        log.error(StrUtil.format("The data is too long, index: {}, raw size: {}, transfer size: {}", e.getIndex(), e.getDataSize(), e.getTransferSize()), e);
        return Result.fail("数据过长");
    }

    @ExceptionHandler(java.sql.SQLDataException.class)
    public Result<Void> sqlDataException(java.sql.SQLDataException e) {
        log.error("SQL data exception", e);
        return Result.fail("SQL 数据错误");
    }

    @ExceptionHandler(java.sql.SQLSyntaxErrorException.class)
    public Result<Void> sqlSyntaxErrorException(java.sql.SQLSyntaxErrorException e) {
        log.error("SQL syntax error", e);
        return Result.error("SQL 语法错误");
    }

    @ExceptionHandler(java.sql.SQLException.class)
    public Result<Void> sqlException(java.sql.SQLException e) {
        log.error("Exception accessing the database", e);
        return Result.fail("SQL 语句错误");
    }

    /* *************************************************** JDBC异常 *************************************************** */
    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    public Result<Void> duplicateKeyException(org.springframework.dao.DuplicateKeyException e) {
        log.error("The data violates a primary key or unique constraint", e);
        return Result.fail("已存在该数据");
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public Result<Void> dataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException e) {
        log.error("Data integrity violations (violation of database constraints)", e);
        return Result.fail("违反数据库约束");
    }

    @ExceptionHandler(org.springframework.dao.CannotAcquireLockException.class)
    public Result<Void> cannotAcquireLockException(org.springframework.dao.CannotAcquireLockException e) {
        log.error("Unable to acquire database lock, check for deadlock", e);
        return Result.fail("获取数据库锁错误");
    }

    @ExceptionHandler(org.springframework.dao.CannotSerializeTransactionException.class)
    public Result<Void> cannotSerializeTransactionException(org.springframework.dao.CannotSerializeTransactionException e) {
        log.error("Serialize transaction exception", e);
        return Result.fail("序列化事务错误");
    }

    @ExceptionHandler(org.springframework.dao.QueryTimeoutException.class)
    public Result<Void> cannotSerializeTransactionException(org.springframework.dao.QueryTimeoutException e) {
        log.error("Database query timed out", e);
        return Result.fail("数据查询超时");
    }

    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public Result<Void> dataAccessException(org.springframework.dao.DataAccessException e) {
        log.error("Data access exception", e);
        return Result.fail("数据访问层错误");
    }

    /* *************************************************** Redis异常 *************************************************** */

    @ExceptionHandler(org.springframework.data.redis.connection.PoolException.class)
    public Result<Void> poolException(org.springframework.data.redis.connection.PoolException e) {
        log.error("Redis connection pool exception", e);
        return Result.fail("Redis连接池错误");
    }

    @ExceptionHandler(org.springframework.data.redis.RedisSystemException.class)
    public Result<Void> redisSystemException(org.springframework.data.redis.RedisSystemException e) {
        log.error("Redis system exception", e);
        return Result.fail("Redis系统错误");
    }

    @ExceptionHandler(org.springframework.data.redis.RedisConnectionFailureException.class)
    public Result<Void> redisConnectionFailureException(org.springframework.data.redis.RedisConnectionFailureException e) {
        log.error("Redis connection exception", e);
        return Result.fail("Redis连接失败");
    }

    /* *************************************************** 其他异常 *************************************************** */

    @ExceptionHandler(java.util.concurrent.RejectedExecutionException.class)
    public Result<Void> rejectedExecutionException(java.util.concurrent.RejectedExecutionException e) {
        log.error("The thread pool is full", e);
        return Result.fail("线程池已满");
    }

    /* *************************************************** 总异常 *************************************************** */

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<Void> exception(Exception e) {
        log.error("System unknown exception", e);
        return Result.fail("系统未知错误，请联系管理员");
    }
}
