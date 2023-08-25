package org.zero.demo.mybatisplus.handler;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import org.zero.common.data.util.mybatisplus.TenantContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author cnzeropro@qq.com
 * @since 2023/3/9
 */
@RequiredArgsConstructor
public class CustomTenantLineHandler implements TenantLineHandler {
    public static final String TENANT_ID = "tenant_id";

    private final String[] ignoreTables;
    private final String[] notIgnoreTables;

    @Override
    public Expression getTenantId() {
        String tenantId = TenantContext.get();
        return new StringValue(tenantId);
    }

    /**
     * 获取租户字段名
     * <p>
     * 默认字段名：tenant_id
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return TENANT_ID;
    }

    /**
     * 根据表名判断是否忽略拼接多租户条件
     * <p>
     * 默认都要进行解析并拼接多租户条件
     *
     * @param tableName 表名
     * @return 是否忽略
     */
    @Override
    public boolean ignoreTable(String tableName) {
        // todo: 如果当前登录用户是超级管理员，始终忽略

        if (ArrayUtils.isEmpty(ignoreTables)) {// 没有配置忽略表
            if (ArrayUtils.isEmpty(notIgnoreTables)) {// 没有配置不忽略表
                return true;
            } else {// 配置了不忽略表
                return Arrays.stream(notIgnoreTables).noneMatch(tableName::equalsIgnoreCase);
            }
        } else {// 当配置了忽略表，始终优先使用其来进行判断
            return Arrays.stream(ignoreTables).anyMatch(tableName::equalsIgnoreCase);
        }
    }

    /**
     * 忽略插入租户字段逻辑
     *
     * @param columns        插入字段
     * @param tenantIdColumn 租户 ID 字段
     * @return 是否忽略
     */
    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }
}
