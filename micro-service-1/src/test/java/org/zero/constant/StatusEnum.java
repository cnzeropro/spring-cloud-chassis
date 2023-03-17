package org.zero.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/8/26 11:30
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {
    NORMAL(1, "正常"),
    LOCKED(2, "锁定"),
    FREEZING(3, "冻结"),
    LOST(4, "挂失"),
    DELETED(5, "销户");

    // MP：数据库存储的值
    @EnumValue
    private final Integer statusCode;
    // Jackson：序列化成json使用的值
    @JsonValue
    private final String statusName;
}
