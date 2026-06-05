package org.marketplace_lea.common.client.wave;

import org.marketplace_lea.common.client.wave.config.WaveClientConfig;
import org.marketplace_lea.common.client.wave.dto.WaveCheckout;
import org.marketplace_lea.common.client.wave.dto.WavePayoutResponse;
import org.marketplace_lea.common.client.wave.form.WaveCheckoutForm;
import org.marketplace_lea.common.client.wave.form.WavePayoutForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${wave.api.name}", url = "${wave.api.url}", configuration = WaveClientConfig.class)
public interface WaveClient {
    @PostMapping(value = "/checkout/sessions", headers = {"Content-Type=application/json", "Authorization=Bearer ${wave.api.key}"})
    WaveCheckout createSession(@RequestBody WaveCheckoutForm form);

    @GetMapping(value = "/checkout/sessions/{sessionId}", headers = {"Content-Type=application/json", "Authorization=Bearer ${wave.api.key}"})
    WaveCheckout getSessionById(@PathVariable String sessionId);

    @PostMapping(value = "/payout", headers = {"Content-Type=application/json", "Authorization=Bearer ${wave.api.key-payout}"})
    WavePayoutResponse payout(@RequestHeader("idempotency-key") String idempotencyKey, @RequestBody WavePayoutForm form);
}
