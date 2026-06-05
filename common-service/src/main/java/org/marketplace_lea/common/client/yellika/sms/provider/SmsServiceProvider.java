package org.marketplace_lea.common.client.yellika.sms.provider;

import org.marketplace_lea.common.client.yellika.sms.YellikaSmsClient;
import org.marketplace_lea.common.client.yellika.sms.form.YellikaSmsForm;
import org.marketplace_lea.common.client.yellika.sms.response.YellikaResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceProvider implements SmsProvider {
    private final YellikaSmsClient yellikaSmsClient;

    @Override
    public void sendSms(YellikaSmsForm form) {
        try {
            YellikaResponse response = yellikaSmsClient.sendSms(form);
            log.info("[SMSServiceProvider.sendSms] Send sms successfully to {}, response: {}", form.recipient(), response.message());
        } catch (FeignException.FeignClientException e) {
            log.error("Error while sending sms to {}: {}", form.recipient(), e.getMessage(), e);
        }
    }
}