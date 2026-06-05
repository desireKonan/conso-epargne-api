package org.marketplace_lea.common.client.wane_delivery;

import org.marketplace_lea.common.client.wane_delivery.exception.WaneDeliveryException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class WaneDeliveryErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();
    
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400) {
            try {
                String body = response.body().toString();
                log.error("Wane Delivery API Error - Status: {}, Method: {}, Body: {}", response.status(), methodKey, response);
                
                return new WaneDeliveryException(
                        String.format("Wane delivery API Error: %s", response.body()),
                        HttpStatus.valueOf(response.status())
                );
            } catch (Exception e) {
                log.error("Error reading error response", e);
            }
        }
        
        return defaultErrorDecoder.decode(methodKey, response);
    }
}