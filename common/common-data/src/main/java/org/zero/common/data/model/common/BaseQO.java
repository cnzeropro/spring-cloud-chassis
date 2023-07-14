package org.zero.common.data.model.common;

import lombok.Data;
import org.zero.common.data.model.common.Page;

import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 前端列表查询对象，两种使用方式：
 * 1、直接使用：直接用于承接前端传入参数
 * 2、继承使用：查询实体继承其并扩展字段
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2023/1/5
 */
@Data
public class BaseQO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    @Positive(message = "当前页码不能小于或等于0")
    private long pageNum = 1L;

    /**
     * 每页显示数
     */
    @Positive(message = "每页数目不能小于或等于0")
    private long pageSize = Page.DEFAULT_PAGE_SIZE;

    /**
     * 需求字段
     */
    private String[] columns = new String[0];

    /**
     * 排序规则
     */
    private Collation[] collations = new Collation[0];

    /**
     * 多值查询
     */
    private MultiVal[] multiVals = new MultiVal[0];

    /**
     * 范围查询
     */
    private Range[] ranges = new Range[0];

    @Data
    public static class Collation {
        /**
         * 排序字段
         */
        private String column;

        /**
         * 排序方式，是否升序，默认true
         */
        private boolean asc = true;
    }

    @Data
    public static class MultiVal {
        /**
         * in查询字段
         */
        private String column;

        /**
         * in查询条件
         */
        private Object[] values = new Object[0];
    }

    @Data
    public static class Range {
        /**
         * 查询字段
         */
        private String column;

        /**
         * 起始的查询条件
         */
        private Object startVal;

        /**
         * 结束的查询条件
         */
        private Object endVal;
    }
}
