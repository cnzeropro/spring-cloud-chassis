package org.zero.demo.spring.boot.quartz.job;

import org.quartz.DisallowConcurrentExecution;

/**
 * 禁止并行的任务
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@DisallowConcurrentExecution
public class NoParallelJob extends BaseJob {
}
