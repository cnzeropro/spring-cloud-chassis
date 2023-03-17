package org.zero.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 带修正的分页对象
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/8/18 9:07
 */
@Data
@Accessors(chain = true)
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 8463126863903128798L;

    /**
     * 默认每页数目：10
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前页码
     */
    private long currentPage = 1L;
    /**
     * 每页数目
     */
    private long pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 总页码
     */
    private long totalPage = 0L;
    /**
     * 记录总数
     */
    private long recordCount = 0L;

    /**
     * 数据对象列表
     */
    private List<T> records = Collections.emptyList();

    private Page() {
        this(1, DEFAULT_PAGE_SIZE);
    }

    private Page(long currentPage, long pageSize) {
        setCurrentPage(currentPage);
        setPageSize(pageSize);
    }

    private Page(long currentPage, long pageSize, long recordCount) {
        this(currentPage, pageSize);
        setRecordCount(recordCount);
    }

    /**
     * 设置并修正当前页码
     */
    public Page<T> setCurrentPage(long currentPage) {
        if (totalPage > 0 && totalPage < currentPage) {
            currentPage = totalPage;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }
        this.currentPage = currentPage;
        return this;
    }

    /**
     * 设置并修正每页条数为默认值
     */
    public Page<T> setPageSize(long pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
        return setTotalPage(totalPage);
    }

    /**
     * 设置并修正总页数
     */
    public Page<T> setTotalPage(long totalPage) {
        if (recordCount > 0 && pageSize > 0) {
            totalPage = recordCount / pageSize;
            if (recordCount % pageSize != 0) {
                totalPage++;
            }
        }
        this.totalPage = totalPage;
        return setCurrentPage(currentPage);
    }

    /**
     * 设置记录条数
     */
    public Page<T> setRecordCount(long recordCount) {
        this.recordCount = recordCount;
        return setTotalPage(totalPage);
    }

    public <R> Page<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> data = this.getRecords().stream().map(mapper).collect(Collectors.toList());
        return ((Page<R>) this).setRecords(data);
    }

    public static <T> Page<T> of() {
        return new Page<>();
    }

    public static <T> Page<T> of(long currentPage) {
        return new Page<>(currentPage, DEFAULT_PAGE_SIZE);
    }

    public static <T> Page<T> of(long currentPage, long pageSize) {
        return new Page<>(currentPage, pageSize);
    }

    public static <T> Page<T> of(long currentPage, long pageSize, long recordCount) {
        return new Page<>(currentPage, pageSize, recordCount);
    }
}
