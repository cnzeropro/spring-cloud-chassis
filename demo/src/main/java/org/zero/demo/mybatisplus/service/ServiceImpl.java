package org.zero.demo.mybatisplus.service;


import lombok.extern.slf4j.Slf4j;
import org.zero.demo.mybatisplus.manager.BaseManager;

import javax.annotation.Resource;

/**
 * @author zero
 * @since 2021-03-07 09:07:48
 */
@Slf4j
public class ServiceImpl<M extends BaseManager<T>, T> implements BaseService<T> {
    @Resource
    protected M baseManager;

    @Override
    public M getBaseManager() {
        return baseManager;
    }
}