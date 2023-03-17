package org.zero.assembly.spring.boot.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/11/30
 */
@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class CustomJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info(context.getMergedJobDataMap().toString());
    }
}
