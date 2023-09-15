package org.zero.demo.mybatisplus.manager;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基于 MP 顶级 Service 的扩展，形成通用 Manager 父类
 *
 * @author zero
 * @since 2021-03-07 09:07:48
 */
public interface BaseManager<T> extends IService<T> {
}