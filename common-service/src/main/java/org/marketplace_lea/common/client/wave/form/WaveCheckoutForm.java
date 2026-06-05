package org.marketplace_lea.common.client.wave.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static org.marketplace_lea.common.common.constants.Router.MOBILE_FAIL_URL;
import static org.marketplace_lea.common.common.constants.Router.MOBILE_SUCCESS_URL;

@Data
public final class WaveCheckoutForm {
    private String amount;

    @JsonProperty("client_reference")
    private String clientReference;

    private String currency = "XOF";

    @JsonProperty("error_url")
    private String errorUrl;

    @JsonProperty("success_url")
    private String successUrl;

    public WaveCheckoutForm(String amount, String clientReference) {
        this.amount = amount;
        this.clientReference = clientReference;
        this.errorUrl = MOBILE_FAIL_URL.replace("fail", clientReference);
        this.successUrl = MOBILE_SUCCESS_URL.replace("success", clientReference);
    }

    public void generateErrorUrl(String mobileFailUrl) {
        this.errorUrl = mobileFailUrl.replace("fail", clientReference);
    }

    public void generateSuccesUrl(String mobileSuccessUrl) {
        this.successUrl = mobileSuccessUrl.replace("success", clientReference);
    }
}
