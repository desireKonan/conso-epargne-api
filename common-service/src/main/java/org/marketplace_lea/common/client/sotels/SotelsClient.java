package org.marketplace_lea.common.client.sotels;

import org.marketplace_lea.common.client.sotels.config.SotelsClientConfig;
import org.marketplace_lea.common.client.sotels.form.SotelsApiForm;
import org.marketplace_lea.common.client.sotels.response.SotelsApiResponse;
import org.marketplace_lea.common.client.sotels.form.SotelsApiStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static org.marketplace_lea.common.common.constants.ConsoEpargneConstants.PAYMENT_API_URL;

@FeignClient(name = "sotelsClient", url = PAYMENT_API_URL, configuration = SotelsClientConfig.class)
public interface SotelsClient {
    @PostMapping
    SotelsApiResponse initiateTransaction(SotelsApiForm form);

    @GetMapping("/{sotelsReference}")
    SotelsApiStatus getTransactionStatus(@PathVariable String sotelsReference);
}
