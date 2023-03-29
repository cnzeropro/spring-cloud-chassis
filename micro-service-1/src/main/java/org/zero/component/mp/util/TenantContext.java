package org.zero.component.mp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedInheritableThreadLocal;

/**
 * @author cnzeropro@qq.com
 * @since 2023/3/9
 */
@Slf4j
public class TenantContext {
    private static final NamedInheritableThreadLocal<String> TENANT_HOLDER = new NamedInheritableThreadLocal<>("Tenant Context");

    public static void set(String tenant) {
        if (log.isDebugEnabled()) {
            log.debug("Set tenant to [{}].", tenant);
        }
        TENANT_HOLDER.set(tenant);
    }

    public static String get() {
        return TENANT_HOLDER.get();
    }

    public static void remove() {
        if (log.isDebugEnabled()) {
            log.debug("Remove tenant.");
        }
        TENANT_HOLDER.remove();
    }
}
