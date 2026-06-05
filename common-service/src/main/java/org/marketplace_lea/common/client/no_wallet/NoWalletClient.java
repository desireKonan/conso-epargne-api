package org.marketplace_lea.common.client.no_wallet;

import org.marketplace_lea.common.client.no_wallet.config.NoWalletFeignConfig;
import org.marketplace_lea.common.client.no_wallet.form.InitiatePaymentNoWalletForm;
import org.marketplace_lea.common.client.no_wallet.response.NoWalletPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "noWalletClient",
    url = "${nowallet.api.url}",
    configuration = NoWalletFeignConfig.class
)
public interface NoWalletClient {
    @PostMapping(value = "/init/payment")
    NoWalletPaymentResponse initiatePayment(@RequestHeader("Authorization") String bearerToken, @RequestBody InitiatePaymentNoWalletForm request);
}