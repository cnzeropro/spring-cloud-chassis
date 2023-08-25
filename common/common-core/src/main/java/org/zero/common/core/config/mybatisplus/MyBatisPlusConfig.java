package org.zero.common.core.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://baomidou.com/">MyBatis-Plus</a>
 * 自动装配：{@link com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration}
 *
 * @author cnzeropro@qq.com
 * @date 2021/6/13
 */
@MapperScan({"org.zero.**.mapper"})
@Configuration(proxyBeanMethods = false)
public class MyBatisPlusConfig {
    @Value("${sys.mybatis-plus.tenant.ignore-tables:}")
    private String[] ignoreTables;

    @Value("${sys.mybatis-plus.tenant.not-ignore-tables:}")
    private String[] notIgnoreTables;

    /**
     * MP插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 乐观锁
        OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);

        // 多租户
        // todo：自定义租户处理逻辑
        // CustomTenantLineHandler tenantLineHandler = new CustomTenantLineHandler(ignoreTables, notIgnoreTables);
        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor(/*tenantLineHandler*/null);
        interceptor.addInnerInterceptor(tenantLineInnerInterceptor);

        // MYSQL 数据库分页
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(1000L);
        paginationInnerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        // SQL 性能规范
        IllegalSQLInnerInterceptor illegalSQLInnerInterceptor = new IllegalSQLInnerInterceptor();
        interceptor.addInnerInterceptor(illegalSQLInnerInterceptor);

        // 防止全表更新与删除
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor);

        return interceptor;
    }

    /**
     * 用了分页插件需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
     * MP 3.4已移除
     */
//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }
}
