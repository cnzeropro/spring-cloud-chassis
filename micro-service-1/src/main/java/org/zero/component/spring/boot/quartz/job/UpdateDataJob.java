package org.zero.component.spring.boot.quartz.job;

import org.quartz.PersistJobDataAfterExecution;

/**
 * 允许更新JobDataMap并且禁止并行的任务
 *
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@PersistJobDataAfterExecution
public class UpdateDataJob extends BaseJob {
}
