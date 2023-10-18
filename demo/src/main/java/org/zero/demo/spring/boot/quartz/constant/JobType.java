package org.zero.demo.spring.boot.quartz.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.zero.demo.spring.boot.quartz.job.NoParallelJob;
import org.zero.demo.spring.boot.quartz.job.NormalJob;
import org.zero.demo.spring.boot.quartz.job.UpdateDataAndNoParallelJob;
import org.zero.demo.spring.boot.quartz.job.UpdateDataJob;

/**
 * @author zero
 * @since 2023/10/17
 */
@Getter
@RequiredArgsConstructor
public enum JobType {
    NORMAL_JOB(1, NormalJob.class),
    NO_PARALLEL_JOB(2, NoParallelJob.class),
    UPDATE_DATA_JOB(3, UpdateDataJob.class),
    UPDATE_DATA_AND_NO_PARALLEL_JOB(4, UpdateDataAndNoParallelJob.class),
    ;

    private final Integer type;
    private final Class<? extends Job> clazz;

    public static Class<? extends Job> getJobClass(Integer type) {
        for (JobType jobType : values()) {
            if (jobType.getType().equals(type)) {
                return jobType.getClazz();
            }
        }
        return null;
    }
}
