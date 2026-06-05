package org.marketplace_lea.common.common.service.notification;

import org.marketplace_lea.common.client.yellika.sms.form.YellikaSmsForm;
import org.marketplace_lea.common.client.yellika.sms.provider.SmsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Async("notificationTaskExecutor")
public class SmsService {
    private final SmsProvider smsProvider;

    public void sendSms(String senderId, String message, String recipient) {
        smsProvider.sendSms(new YellikaSmsForm(senderId, message, recipient));
    }
}
