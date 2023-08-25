package org.zero.demo.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.time.Duration;
import java.util.Objects;

/**
 * mybatis拦截器
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = StatementHandler.class, method = "getBoundSql", args = {}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class CustomInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        Method method = invocation.getMethod();
        Object[] args = invocation.getArgs();
        if (target instanceof Executor) {// Executor
            final Executor executor = (Executor) target;
//            boolean isUpdateMethod = args.length == 2;
            boolean isUpdateMethod = "update".equals(method.getName());
            MappedStatement mappedStatement = (MappedStatement) args[0];
            Object param = args[1];
            String sqlId = mappedStatement.getId();
            BoundSql boundSql = mappedStatement.getBoundSql(param);
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

            // 记录sqlId
            if (log.isDebugEnabled()) {
                log.debug("SqlId: {}", sqlId);
            }
            // 参数为ParamMap时做特殊处理
            if (param instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<?> paramMap = (MapperMethod.ParamMap<?>) param;
                if (paramMap.containsKey("et")) {// mp处理
                    param = paramMap.get("et");
                } else {
                    param = paramMap.get("param1");
                }
            }
            // 参数为空时不做处理
            if (Objects.isNull(param)) {
                return logAndInvoke(invocation);
            }
            // 处理
            if (isUpdateMethod) {// 数据更新
                logSql(boundSql);
                if (sqlCommandType == SqlCommandType.INSERT) {
                    // TODO 新增处理
                } else if (sqlCommandType == SqlCommandType.UPDATE) {
                    // TODO 更新处理
                } else if (sqlCommandType == SqlCommandType.DELETE) {
                    // TODO 删除处理
                }
            } else if (sqlCommandType == SqlCommandType.SELECT) {// 数据查询
                RowBounds rowBounds = (RowBounds) args[2];
                ResultHandler resultHandler = (ResultHandler) args[3];
                // 几乎不可能走进这里面，除非使用Executor的代理对象调用query[args[6]]
                if (args.length != 4) {
                    boundSql = (BoundSql) args[5];
                }
                logSql(boundSql);
                // TODO 查询处理
            }
        } else {// StatementHandler
            final StatementHandler statementHandler = (StatementHandler) target;
            // 目前只有StatementHandler.getBoundSql方法args才为null
            if (Objects.isNull(args) || args.length == 0) {
                // TODO getBoundSql方法的处理
            } else {
                Connection connection = (Connection) args[0];
                Integer transactionTimeout = (Integer) args[1];
                // TODO prepare方法的处理
            }
        }

        return logAndInvoke(invocation);
    }

    /**
     * 记录sql语句
     */
    private void logSql(BoundSql boundSql) {
        if (log.isDebugEnabled()) {
            String sql = boundSql.getSql();
            log.debug("SQL: {}", sql);
        }
    }

    /**
     * 记录sql调用时间
     */
    private Object logAndInvoke(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        if (log.isDebugEnabled()) {
            log.debug("Start executing SQL");
        }

        long start = System.nanoTime();
        Object result = invocation.proceed();
        long end = System.nanoTime();

        if (log.isDebugEnabled()) {
            log.debug("SQL execution complete, Time: {}", Duration.ofNanos(end - start));
        }

        return result;
    }
}
