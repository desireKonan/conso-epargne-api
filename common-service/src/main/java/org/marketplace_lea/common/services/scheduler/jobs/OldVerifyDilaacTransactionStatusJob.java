package org.marketplace_lea.common.services.scheduler.jobs;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OldVerifyDilaacTransactionStatusJob implements Job {
    //private final PaymentGatewayService paymentGatewayService;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        if(jobDataMap != null) {
            String transactionId = jobDataMap.get("transactionId").toString();
            String transactionType = jobDataMap.get("transactionType").toString();
            try {
                // paymentGatewayService.verifyDilaacTransactionAndUpdateStatusIfSuccess(transactionId, transactionType);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
