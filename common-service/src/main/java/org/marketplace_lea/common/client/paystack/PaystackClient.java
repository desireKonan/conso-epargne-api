package org.marketplace_lea.common.client.paystack;

import org.marketplace_lea.common.client.paystack.config.PaystackFeignConfig;
import org.marketplace_lea.common.client.paystack.form.CustomerForm;
import org.marketplace_lea.common.client.paystack.form.InitializeTransactionForm;
import org.marketplace_lea.common.client.paystack.response.CustomerResponse;
import org.marketplace_lea.common.client.paystack.response.InitializeTransactionResponse;
import org.marketplace_lea.common.client.paystack.response.PaystackResponse;
import org.marketplace_lea.common.client.paystack.response.TransactionListResponse;
import org.marketplace_lea.common.client.paystack.response.TransactionVerifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "paystackClient",
    url = "${paystack.api.base-url}",
    configuration = PaystackFeignConfig.class
)
public interface PaystackClient {
    // Transactions
    @PostMapping("/transaction/initialize")
    PaystackResponse<InitializeTransactionResponse> initializeTransaction(@RequestBody InitializeTransactionForm request);
    
    @GetMapping("/transaction/verify/{reference}")
    PaystackResponse<TransactionVerifyResponse> verifyTransaction(@PathVariable("reference") String reference);
    
    @GetMapping("/transaction")
    PaystackResponse<TransactionListResponse> listTransactions(@RequestParam("page") int page, @RequestParam("perPage") int perPage);
    
    @GetMapping("/transaction/{id}")
    PaystackResponse<TransactionVerifyResponse> fetchTransaction(@PathVariable("id") String transactionId);
    
    // Customers
    @PostMapping("/customer")
    PaystackResponse<CustomerResponse> createCustomer(@RequestBody CustomerForm request);
    
    @GetMapping("/customer/{email}")
    ResponseEntity<PaystackResponse<CustomerResponse>> fetchCustomer(@PathVariable("email") String email);
}