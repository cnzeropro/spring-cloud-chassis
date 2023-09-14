package org.zero.log.common.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.zero.common.data.util.javax.net.IpUtil;
import org.zero.common.data.util.spring.RequestUtil;
import org.zero.common.data.util.spring.SpELUtil;
import org.zero.log.common.annotation.AutoLog;
import org.zero.log.common.event.SysLogEvent;
import org.zero.log.common.model.SysLogPO;
import org.zero.log.common.util.LogUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zero
 * @date 2022/1/3
 */
@Aspect
@Slf4j
public class AutoLogAspect implements InitializingBean {
    private ObjectMapper objectMapper;

    @Around("@annotation(autoLog)")
    public Object around(ProceedingJoinPoint joinPoint, AutoLog autoLog) throws Throwable {
        // 获取方法参数值
        Object[] args = joinPoint.getArgs();
        // 只会注解到方法上，所以直接安心强转
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取方法对象
        Method method = signature.getMethod();

        // 构建SysLog对象
        SysLogPO sysLog = SysLogPO.builder()
                .app(autoLog.app())
                .module(autoLog.module())
                .type(autoLog.type().name())
                .operateType(autoLog.operateType().name())
                .normal(Boolean.TRUE)
                .build();

        // 获取指定SpEL表达式值
        String value = autoLog.value();
        try {
            value = SpELUtil.evaluate(method, args, value, String.class);
        } catch (Exception e) {
            log.warn(String.format("@AutoLog parse SpEL[%s] error", value), e);
        }
        sysLog.setMessage(value);

        // 设置相关参数信息
        setParamInfo(sysLog, method, args, autoLog.withParam(), autoLog.excludedParamNames());

        // String username = SpringSecurityUtil.getUsername();
        // todo: 调用iam服务api查询用户信息
        String username = null;
        sysLog.setOperator(username);

        // 发送异步日志事件
        long endTime = 0L;
        Object result = null;
        LocalDateTime startDateTime = LocalDateTime.now();
        long startTime = System.nanoTime();
        try {
            result = joinPoint.proceed();
            endTime = System.nanoTime();
        } catch (Throwable e) {
            endTime = System.nanoTime();
            sysLog.setNormal(Boolean.FALSE);
            sysLog.setException(e.getMessage());
            throw e;
        } finally {
            if (autoLog.withReturnData()) {
                sysLog.setReturnData(toExpectedStr(result));
            }
            sysLog.setExecutionTime(startDateTime);
            sysLog.setCostTime(endTime - startTime);
            // 发布事件
            SpringUtil.publishEvent(new SysLogEvent(sysLog));
        }

        return result;
    }

    private void setParamInfo(SysLogPO sysLog,
                              Method method, Object[] args,
                              boolean getParam, String[] excludedParamNames)
            throws IOException {
        Map<String, Object> paramMap = MapUtil.newHashMap();

        // 封装方法的参数信息
        if (getParam) {
            Map<String, Object> methodParameterMap = MapUtil.newHashMap(true);
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                String parameterName = parameters[i].getName();
                if (!ArrayUtil.contains(excludedParamNames, parameterName)) {
                    methodParameterMap.put(parameterName, args[i]);
                }
            }
            paramMap.put("methodParam", methodParameterMap);
        }

        RequestUtil.getHttpServletRequestOpt().ifPresent(request -> {
            // 封装请求相关信息
            sysLog.setRemoteAddr(IpUtil.getRemoteIp(request));
            sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
            sysLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
            sysLog.setRequestMethod(request.getMethod());

            if (getParam) {
                // 封装查询字符串（URL参数）（GET、DELETE等请求）
                String queryStr = LogUtil.excludeInQueryStr(request.getQueryString(), excludedParamNames);
                Map<String, List<String>> queryStrParam = HttpUtil.decodeParams(queryStr, StandardCharsets.UTF_8);
                paramMap.put("queryStr", queryStr);
                paramMap.put("queryParam", queryStrParam);

                // 封装请求体参数（POST、PUT等请求），目前只处理了 application/json、multipart/form-data、application/x-www-form-urlencoded
                String contentType = request.getContentType();
                // application/x-www-form-urlencoded、multipart/form-data
                if (isFormUrlencoded(contentType) || isMultipart(contentType, false)) {
                    Map<String, String[]> requestBodyParamMap = MapUtil.newHashMap();
                    // 因为javax.servlet.ServletRequest.getParameterMap()方法会同时取到查询字符串和请求体里的参数，所以需要排除查询字符串的参数
                    Map<String, String[]> parameterMap = request.getParameterMap();
                    Set<String> queryParamKeySet = queryStrParam.keySet();
                    Set<String> parameterKeySet = parameterMap.keySet();
                    Collection<String> intersection = CollUtil.intersection(queryParamKeySet, parameterKeySet);
                    // Hutool工具提供的subtract()方法存在clone，会导致parameterKeySet拷贝报错
                    // Collection<String> subtract = CollUtil.subtract(parameterKeySet, queryParamKeySet);
                    Collection<String> subtract = this.subtract(parameterKeySet, queryParamKeySet);
                    intersection.forEach(s -> {
                        List<String> queryParamValues = queryStrParam.get(s);
                        String[] parameterValues = parameterMap.get(s);
                        List<String> requestBodyParams = ListUtil.list(true, parameterValues);
                        // temp.removeAll(queryParamValues);
                        for (String queryParamValue : queryParamValues) {
                            requestBodyParams.remove(queryParamValue);
                        }
                        if (CollUtil.isNotEmpty(requestBodyParams)) {
                            requestBodyParamMap.put(s, requestBodyParams.toArray(new String[0]));
                        }
                    });
                    subtract.forEach(s -> requestBodyParamMap.put(s, parameterMap.get(s)));

                    // 判断请求是否为文件上传类型
                    if (request instanceof MultipartHttpServletRequest) {
                        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                        // 获取文件参数
                        requestBodyParamMap.putAll(multipartRequest.getMultiFileMap()
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()
                                        .stream()
                                        .map(MultipartFile::getOriginalFilename)
                                        .toArray(String[]::new)
                                )));
                    }

                    Map<String, String[]> excludedRequestBodyParamMap = LogUtil.excludeInParamMap(requestBodyParamMap, excludedParamNames);
                    paramMap.put("requestBody", excludedRequestBodyParamMap);
                }
                // application/json
                else if (isJson(contentType)) {
                    String requestBody = readRequestBody(request);
                    try {
                        JsonNode jsonNode = objectMapper.readTree(requestBody);
                        LogUtil.excludeInJson(jsonNode, excludedParamNames);
                        paramMap.put("requestBody", jsonNode);
                    } catch (IOException e) {
                        log.warn("Failed to parse request body", e);
                        paramMap.put("requestBody", requestBody);
                    }
                } else {
                    paramMap.put("requestBody", readRequestBody(request));
                }
            }
        });

        String param = objectMapper.writeValueAsString(paramMap);
        sysLog.setParam(param);
    }

    private boolean isJson(String contentType) {
        return CharSequenceUtil.startWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE);
    }

    private boolean isFormUrlencoded(String contentType) {
        return CharSequenceUtil.startWithIgnoreCase(contentType, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    private boolean isMultipart(String contentType, boolean strictServletCompliance) {
        return CharSequenceUtil.startWithIgnoreCase(contentType, strictServletCompliance ? MediaType.MULTIPART_FORM_DATA_VALUE : "multipart/");
    }

    private <T> Collection<T> subtract(Collection<T> coll1, Collection<T> coll2) {
        if (CollUtil.isNotEmpty(coll1) && CollUtil.isNotEmpty(coll2)) {
            Collection<T> result = new LinkedList<>(coll1);
            result.removeAll(coll2);
            return result;
        }
        return new ArrayList<>();
    }

    private String readRequestBody(HttpServletRequest request) {
        String requestBody = "";

        try {
            requestBody = IoUtil.read(request.getInputStream(), false).toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.warn("Failed to read RequestBody from ServletInputStream", e);
        }

        try {
            requestBody = IoUtil.read(request.getReader(), false);
        } catch (IOException e) {
            log.warn("Failed to read RequestBody from BufferedReader", e);
        }

        return requestBody;
    }

    private String toExpectedStr(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ignored) {
        }

        return obj.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 从容器中的对象copy而来，因为要进行配置调整，避免影响到全局
        objectMapper = SpringUtil.getBean(ObjectMapper.class).copy();
        // 序列化对象的所有属性，包括为Null的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 关闭 序列化时间日期为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 关闭 空对象报错（对应属性没有get方法）
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
