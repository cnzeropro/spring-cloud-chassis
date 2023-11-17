package org.zero.demo.spring.boot.quartz.service;

import org.zero.demo.spring.boot.quartz.model.SysJob;

import java.util.List;

/**
 * @author zero
 * @since 2022/7/24
 */
public interface SysJobService {
    List<SysJob> listEnabled();

    boolean add(SysJob sysJob);
}
