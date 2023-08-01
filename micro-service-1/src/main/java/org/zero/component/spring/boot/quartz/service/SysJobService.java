package org.zero.component.spring.boot.quartz.service;

import org.zero.component.spring.boot.quartz.model.SysJob;

/**
 * @author zero
 * @since 2022/7/24
 */
public interface SysJobService {
    boolean add(SysJob sysJob);
}
