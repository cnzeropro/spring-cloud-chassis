package org.zero.component.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zero.component.mybatisplus.util.TenantContext;

import java.util.List;

/**
 * @author cnzeropro@qq.com
 * @since 2023/3/9
 */
@Component
public class CustomTenantLineHandler implements TenantLineHandler {
    public static final String TENANT_ID = "tenant_id";

    @Value("${mp.tenant.ignore-tables:}")
    private List<String> ignoreTables;

    @Value("${mp.tenant.not-ignore-tables:}")
    private List<String> notIgnoreTables;

    @Override
    public Expression getTenantId() {
        String tenantId = TenantContext.get();
        return new StringValue(tenantId);
    }

    /**
     * 获取租户字段名
     * <p>
     * 默认字段名叫: tenant_id
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
     * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
     */
    @Override
    public boolean ignoreTable(String tableName) {
        // todo: 如果当前登录用户是超级管理员，始终忽略

        // 配置了忽略表和不忽略表
        if (ignoreTables.isEmpty()) {
            if (notIgnoreTables.isEmpty()) {
                return true;
            } else {
                return !notIgnoreTables.contains(tableName);
            }
        } else {
            // 当ignoreTables不为空时，始终优先使用来进行判断
            return ignoreTables.contains(tableName);
        }
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }
}
