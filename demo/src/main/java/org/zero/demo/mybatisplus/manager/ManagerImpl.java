package org.zero.demo.mybatisplus.manager;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zero
 * @since 2021-03-07 09:07:48
 */
@Slf4j
public class ManagerImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseManager<T> {
}