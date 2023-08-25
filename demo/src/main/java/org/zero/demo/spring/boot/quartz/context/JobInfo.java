package org.zero.demo.spring.boot.quartz.context;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * @author zero
 * @since 2023/8/1
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class JobInfo {
    /**
     * 参数（JSON）
     */
    private Map<String, Object> parma;
    /**
     * 执行结果
     */
    private Object result;
}
