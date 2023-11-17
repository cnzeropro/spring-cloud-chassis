package org.zero.demo.spring.boot.web.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.zero.common.data.model.Result;
import org.zero.common.data.util.javax.web.ResponseUtil;
import org.zero.common.data.util.spring.JacksonUtils;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * @author zero
 * @since 2019/10/18
 */
@Slf4j
@RestController
@RequestMapping("async")
public class AsyncController {
    /**
     * servlet（3.0）原生api支持
     */
    @GetMapping("/a0")
    public void a0(HttpServletRequest request) {
        // 开启异步
        AsyncContext asyncContext = request.startAsync();
        // 设置超时时间
        asyncContext.setTimeout(3000L);
        // 设置监听
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                log.info("Complete");
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                log.info("Timeout");
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                Throwable throwable = event.getThrowable();
                log.warn("Error", throwable);
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                log.info("StartAsync");
            }
        });
        // 运行
        asyncContext.start(() -> {
            Result<Integer> result = Result.error();
            // 模拟1秒耗时
            try {
                Thread.sleep(1000L);
                result = Result.ok(1);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            ResponseUtil.writeJson(asyncContext.getResponse(), JacksonUtils.toJsonStr(result));
            // 完成异步请求处理
            asyncContext.complete();
        });
    }

    /**
     * Spring 支持，简单
     */
    @GetMapping("/a1")
    public Callable<Result<Integer>> a1() {
        return () -> {
            // 模拟1秒耗时
            try {
                Thread.sleep(1000L);
                return Result.ok(1);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                return Result.error();
            }
        };
    }

    /**
     * Spring 支持，灵活，但需要自定义线程或者线程池
     */
    @GetMapping("/a2")
    public DeferredResult<Result<Integer>> a2() {
        // 超时3秒，超时后返回error
        DeferredResult<Result<Integer>> deferredResult = new DeferredResult<>(3000L);
        deferredResult.onTimeout(() -> Result.error("请求超时"));
        deferredResult.onError(throwable -> log.warn("Error", throwable));
        deferredResult.onCompletion(() -> log.info("Complete"));
        new Thread(() -> {
            // 模拟1秒耗时
            try {
                Thread.sleep(1000L);
                deferredResult.setResult(Result.ok(1));
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                deferredResult.setResult(Result.error());
            }
        }).start();
        return deferredResult;
    }

    /**
     * Spring 支持，灵活，无需自定义线程或者线程池
     */
    @GetMapping("/a3")
    public WebAsyncTask<Result<Integer>> a3() {
        WebAsyncTask<Result<Integer>> webAsyncTask = new WebAsyncTask<>(3000L, () -> {
            try {
                Thread.sleep(1000L);
                return Result.ok(1);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                return Result.error();
            }
        });
        webAsyncTask.onTimeout(() -> Result.error("请求超时"));
        webAsyncTask.onError(() -> Result.error("请求失败"));
        webAsyncTask.onCompletion(() -> log.info("Complete"));
        return webAsyncTask;
    }
}
