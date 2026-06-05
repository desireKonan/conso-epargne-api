package org.marketplace_lea.common.client.paystack.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionVerifyResponse {
    private Long id;
    private String status;
    private String reference;
    private Long amount;
    private String gateway_response;
    private LocalDateTime paid_at;
    private LocalDateTime created_at;
    private String channel;
    private String currency;
    private String ip_address;
    private Customer customer;
    private Authorization authorization;
    private Log log;
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Customer {
        private Long id;
        private String email;
        private String customer_code;
        private String first_name;
        private String last_name;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Authorization {
        private String authorization_code;
        private String card_type;
        private String last4;
        private String exp_month;
        private String exp_year;
        private String channel;
        private String reusable;
        private String signature;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Log {
        private Integer time_spent;
        private Integer attempts;
        private String authentication;
        private Integer errors;
        private Boolean success;
        private Boolean mobile;
        private List<Object> history;
    }
}