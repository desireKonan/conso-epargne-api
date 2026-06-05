package org.marketplace_lea.common.common.constants;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

/**
 * Constantes pour le service de scheduling
 */
public interface SchedulerConstants {
    // Groupes
    String DILAAC_STATUS_CHECK_GROUP = "DILAAC-PAYMENT-STATUS-JOB";
    String DILAAC_STATUS_CHECK_TRIGGER_GROUP = "DILAAC-PAYMENT-STATUS-TRIGGER";
    String CUSTOM_STATUS_CHECK_GROUP = "CUSTOM-PAYMENT-STATUS-JOB";
    String CUSTOM_STATUS_CHECK_TRIGGER_GROUP = "CUSTOM-PAYMENT-STATUS-TRIGGER";
    String CLEANUP_GROUP = "PAYMENT-CLEANUP-JOB";
    String CLEANUP_TRIGGER_GROUP = "PAYMENT-CLEANUP-TRIGGER";
    
    // Clés
    String JOB_KEY_SUFFIX = "-dilaac-status-check-job";
    String TRIGGER_KEY_SUFFIX = "-dilaac-status-check-trigger";
    String CUSTOM_JOB_KEY_SUFFIX = "-custom-status-check-job";
    String CUSTOM_TRIGGER_KEY_SUFFIX = "-custom-status-check-trigger";
    
    // Job data keys
    String JOB_DATA_TRANSACTION_ID = "transactionId";
    String JOB_DATA_TRANSACTION_TYPE = "transactionType";
    String JOB_DATA_PROVIDER_TYPE = "providerType";
    String JOB_DATA_MAX_RETRIES = "maxRetries";
    String JOB_DATA_CURRENT_RETRY = "currentRetry";
    
    // Job keys
    JobKey CLEANUP_JOB_KEY = new JobKey("expired-transactions-cleanup", CLEANUP_GROUP);
    TriggerKey CLEANUP_TRIGGER_KEY = new TriggerKey("daily-cleanup-trigger", CLEANUP_TRIGGER_GROUP);
    
    // Configurations par défaut
    int DEFAULT_REPEAT_COUNT = 720; // 1 heure (720 * 5 secondes)
    int DEFAULT_INTERVAL_SECONDS = 5;
    int CLEANUP_INTERVAL_HOURS = 24;
    int MAX_RETRY_COUNT = 3;
    int RETRY_INTERVAL_MINUTES = 5;
    
    // Statuts
    String JOB_STATUS_SCHEDULED = "SCHEDULED";
    String JOB_STATUS_RUNNING = "RUNNING";
    String JOB_STATUS_COMPLETED = "COMPLETED";
    String JOB_STATUS_FAILED = "FAILED";
    String JOB_STATUS_STOPPED = "STOPPED";
}