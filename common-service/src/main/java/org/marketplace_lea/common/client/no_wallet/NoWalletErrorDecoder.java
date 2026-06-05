package org.marketplace_lea.common.client.no_wallet;

import org.marketplace_lea.common.client.no_wallet.exception.NoWalletException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class NoWalletErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();
    
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400) {
            try {
                String body = response.body().toString();
                log.error("Paystack API Error - Status: {}, Method: {}, Body: {}", response.status(), methodKey, body);
                
                return new NoWalletException(
                        String.format("Paystack API Error: %s", response.reason()),
                        HttpStatus.valueOf(response.status())
                );
            } catch (Exception e) {
                log.error("Error reading error response", e);
            }
        }
        
        return defaultErrorDecoder.decode(methodKey, response);
    }
}