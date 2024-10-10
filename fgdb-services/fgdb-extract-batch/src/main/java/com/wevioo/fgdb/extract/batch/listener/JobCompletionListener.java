package com.wevioo.fgdb.extract.batch.listener;

import com.wevioo.fgdb.extract.batch.configuration.DirectoryService;
import com.wevioo.fgdb.extract.batch.configuration.DirectoryStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobCompletionListener implements JobExecutionListener {


    @Autowired
    private DirectoryService directoryService;


    private Long idDirectory;


    @Override
    public void beforeJob(JobExecution jobExecution) {
        idDirectory = jobExecution.getJobParameters().getLong("idRepository");

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            // Handle failed job, or do nothing if you don't want to update in case of failure
            return;
        }
        directoryService.updateRepertoryStatus(idDirectory,DirectoryStatus.TERMINATED);
        log.info("after job..............!");
    }
}
