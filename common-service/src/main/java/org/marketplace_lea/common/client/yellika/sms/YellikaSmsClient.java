package org.marketplace_lea.common.client.yellika.sms;

import org.marketplace_lea.common.client.yellika.sms.config.YellikaSmsConfig;
import org.marketplace_lea.common.client.yellika.sms.form.YellikaSmsForm;
import org.marketplace_lea.common.client.yellika.sms.response.YellikaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "yellikaSmsClient",
        url = "${yellika.sms.url}",
        configuration = YellikaSmsConfig.class
)
public interface YellikaSmsClient {
    @PostMapping("/api/v3/sms/send")
    YellikaResponse sendSms(@RequestBody YellikaSmsForm form);
}
