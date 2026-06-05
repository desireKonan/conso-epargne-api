package org.marketplace_lea.common.services.scheduler.services.impl;

import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;
import org.marketplace_lea.common.services.scheduler.jobs.CleanupExpiredTransactionsJob;
import org.marketplace_lea.common.common.exceptions.SchedulerException;
import org.marketplace_lea.common.services.scheduler.jobs.OldVerifyDilaacTransactionStatusJob;
import org.marketplace_lea.common.services.scheduler.services.PaymentSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

import static org.marketplace_lea.common.common.constants.SchedulerConstants.CLEANUP_INTERVAL_HOURS;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.CLEANUP_JOB_KEY;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.CLEANUP_TRIGGER_KEY;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.CUSTOM_JOB_KEY_SUFFIX;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.CUSTOM_STATUS_CHECK_GROUP;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.CUSTOM_STATUS_CHECK_TRIGGER_GROUP;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.CUSTOM_TRIGGER_KEY_SUFFIX;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.DEFAULT_INTERVAL_SECONDS;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.DEFAULT_REPEAT_COUNT;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.DILAAC_STATUS_CHECK_GROUP;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.DILAAC_STATUS_CHECK_TRIGGER_GROUP;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.JOB_DATA_TRANSACTION_ID;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.JOB_DATA_TRANSACTION_TYPE;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.JOB_KEY_SUFFIX;
import static org.marketplace_lea.common.common.constants.SchedulerConstants.TRIGGER_KEY_SUFFIX;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentSchedulerServiceImpl implements PaymentSchedulerService {
    private final Scheduler scheduler;

    @Override
    public void scheduleDilaacTransactionStatusCheck(TransactionV2Entity transaction) {
        verifyTransaction(transaction);

        try {
            log.info("Scheduling Dilaac status check for transaction: {}", transaction.getId());
            JobDetail jobDetail = createDilaacStatusCheckJob(transaction);
            Trigger trigger = createDilaacStatusCheckTrigger(transaction);
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Dilaac status check scheduled successfully for transaction: {}", transaction.getId());
        } catch (org.quartz.SchedulerException e) {
            log.error("Failed to schedule Dilaac status check for transaction: {}", transaction.getId(), e);
            throw new SchedulerException("Failed to schedule status check", e);
        }
    }

    @Override
    public void stopDilaacTransactionStatusCheck(String transactionId) throws SchedulerException {
        try {
            JobKey jobKey = buildJobKey(transactionId);

            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
                log.info("Stopped Dilaac status check for transaction: {}", transactionId);
            } else {
                log.warn("No active scheduler job found for transaction: {}", transactionId);
            }

        } catch (org.quartz.SchedulerException e) {
            log.error("Failed to stop Dilaac status check for transaction: {}",
                    transactionId, e);
            throw new SchedulerException("Failed to stop status check", e);
        }
    }

    @Override
    public boolean isDilaacStatusCheckActive(String transactionId) {
        try {
            JobKey jobKey = buildJobKey(transactionId);
            return scheduler.checkExists(jobKey);
        } catch (org.quartz.SchedulerException e) {
            log.error("Failed to check scheduler status for transaction: {}", transactionId, e);
            throw new SchedulerException("Failed to check scheduler status", e);
        }
    }

    @Override
    public void scheduleStatusCheck(String transactionId, String transactionType, int repeatCount, int intervalSeconds) {
        try {
            JobDetail jobDetail = createCustomStatusCheckJob(transactionId, transactionType);
            Trigger trigger = createCustomStatusCheckTrigger(transactionId, repeatCount, intervalSeconds);
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Custom status check scheduled for transaction: {} (repeats: {}, interval: {}s)", transactionId, repeatCount, intervalSeconds);
        } catch (org.quartz.SchedulerException e) {
            log.error("Failed to schedule custom status check for transaction: {}", transactionId, e);
            throw new SchedulerException("Failed to schedule custom status check", e);
        }
    }

    @Override
    public void scheduleExpiredTransactionsCleanup() {
        try {
            JobDetail jobDetail = createCleanupJob();
            Trigger trigger = createCleanupTrigger();
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Expired transactions cleanup job scheduled");
        } catch (org.quartz.SchedulerException e) {
            log.error("Failed to schedule expired transactions cleanup job", e);
            throw new SchedulerException("Failed to schedule cleanup job", e);
        }
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private JobDetail createDilaacStatusCheckJob(TransactionV2Entity transaction) {
        return JobBuilder.newJob()
                .ofType(OldVerifyDilaacTransactionStatusJob.class)
                .storeDurably()
                .usingJobData(JOB_DATA_TRANSACTION_ID, transaction.getId())
                .usingJobData(JOB_DATA_TRANSACTION_TYPE, transaction.getTransactionType().name())
                .withIdentity(buildJobKey(transaction.getId()))
                .withDescription(String.format("Verify Dilaac payment status for transaction id '%s'", transaction.getId()))
                .build();
    }

    private Trigger createDilaacStatusCheckTrigger(TransactionV2Entity transaction) {
        return TriggerBuilder.newTrigger()
                .withIdentity(buildTriggerKey(transaction.getId()))
                .withDescription(String.format("Trigger Dilaac status check for transaction id '%s'", transaction.getId()))
                .withSchedule(simpleSchedule()
                        .withRepeatCount(DEFAULT_REPEAT_COUNT)
                        .withIntervalInSeconds(DEFAULT_INTERVAL_SECONDS))
                .build();
    }

    private JobDetail createCustomStatusCheckJob(String transactionId, String transactionType) {
        return JobBuilder.newJob()
                .ofType(OldVerifyDilaacTransactionStatusJob.class)
                .storeDurably()
                .usingJobData(JOB_DATA_TRANSACTION_ID, transactionId)
                .usingJobData(JOB_DATA_TRANSACTION_TYPE, transactionType)
                .withIdentity(buildCustomJobKey(transactionId))
                .withDescription(String.format("Custom status check for transaction id '%s'", transactionId))
                .build();
    }

    private Trigger createCustomStatusCheckTrigger(String transactionId, int repeatCount, int intervalSeconds) {
        return TriggerBuilder.newTrigger()
                .withIdentity(buildCustomTriggerKey(transactionId))
                .withDescription(String.format("Custom trigger for transaction id '%s'", transactionId))
                .withSchedule(simpleSchedule()
                        .withRepeatCount(repeatCount)
                        .withIntervalInSeconds(intervalSeconds))
                .build();
    }

    private JobDetail createCleanupJob() {
        return JobBuilder.newJob()
                .ofType(CleanupExpiredTransactionsJob.class)
                .storeDurably()
                .withIdentity(CLEANUP_JOB_KEY)
                .withDescription("Cleanup expired payment transactions")
                .build();
    }

    private Trigger createCleanupTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(CLEANUP_TRIGGER_KEY)
                .withDescription("Daily cleanup trigger for expired transactions")
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(CLEANUP_INTERVAL_HOURS)
                        .repeatForever())
                .build();
    }

    private JobKey buildJobKey(String transactionId) {
        return new JobKey(transactionId + JOB_KEY_SUFFIX, DILAAC_STATUS_CHECK_GROUP);
    }

    private TriggerKey buildTriggerKey(String transactionId) {
        return new TriggerKey(transactionId + TRIGGER_KEY_SUFFIX, DILAAC_STATUS_CHECK_TRIGGER_GROUP);
    }

    private JobKey buildCustomJobKey(String transactionId) {
        return new JobKey(
                transactionId + CUSTOM_JOB_KEY_SUFFIX,
                CUSTOM_STATUS_CHECK_GROUP
        );
    }

    private TriggerKey buildCustomTriggerKey(String transactionId) {
        return new TriggerKey(
                transactionId + CUSTOM_TRIGGER_KEY_SUFFIX,
                CUSTOM_STATUS_CHECK_TRIGGER_GROUP
        );
    }


    private void verifyTransaction(TransactionV2Entity transaction) {
        if (transaction == null) {
            throw new SchedulerException("Transaction cannot be null");
        }
    }
}