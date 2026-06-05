package org.marketplace_lea.common.services.scheduler.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CleanupExpiredTransactionsJob implements Job {
    // private final Transaction transactionService;
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Starting expired transactions cleanup job");
            int deletedCount = 0;
            log.info("Expired transactions cleanup completed. Deleted {} transactions", deletedCount);
        } catch (Exception e) {
            log.error("Error executing expired transactions cleanup job", e);
            throw new JobExecutionException(e);
        }
    }
}