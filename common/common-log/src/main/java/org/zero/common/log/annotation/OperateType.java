package org.zero.common.log.annotation;

/**
 * @author zero
 * @since 2021/7/21
 */
public enum OperateType {
    /**
     * C：新增
     */
    CREATE,
    /**
     * R：查询
     */
    READ,
    /**
     * U：更新
     */
    UPDATE,
    /**
     * D：删除
     */
    DELETE,

    /**
     * C：导入
     */
    IMPORT,
    /**
     * R：导出
     */
    EXPORT,
    /**
     * D：清空
     */
    CLEAR,

    /**
     * 默认
     */
    OTHER,
    ;
}
