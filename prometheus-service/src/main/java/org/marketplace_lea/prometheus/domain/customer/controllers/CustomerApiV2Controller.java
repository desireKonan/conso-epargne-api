package org.marketplace_lea.prometheus.domain.customer.controllers;


import org.marketplace_lea.common.common.constants.Router;
import org.marketplace_lea.common.dtos.CustomerV2DTO;
import org.marketplace_lea.prometheus.domain.customer.forms.CustomerLockForm;
import org.marketplace_lea.prometheus.domain.customer.forms.CustomerSearchForm;
import org.marketplace_lea.prometheus.domain.customer.services.CustomerApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(Router.CUSTOMERS_URI)
public class CustomerApiV2Controller {
    private final CustomerApiService customerApiService;

    @GetMapping()
    public ResponseEntity<Page<CustomerV2DTO>> fetchCustomers(
            @ModelAttribute @ParameterObject CustomerSearchForm searchForm,
            @PageableDefault @ParameterObject Pageable pageable
    ) {
        log.info("Get Customer Infos by form info: {}", searchForm);
        var customersPage = customerApiService.fetchCustomer(searchForm, pageable);
        return ResponseEntity.ok(customersPage);
    }


    @GetMapping(value = "/{customerId}")
    public ResponseEntity<CustomerV2DTO> fetchCustomerDetails(@PathVariable("customerId") String customerId) {
        log.info("Get Customer Infos by customer id: {}", customerId);
        var customersPage = customerApiService.findById(customerId);
        return ResponseEntity.ok(customersPage);
    }


    @DeleteMapping(value = "/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") String customerId) {
        log.info("Get Customer Infos by customer id: {}", customerId);
        customerApiService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping(value = "/unlock")
    public ResponseEntity<Void> unlockCustomer(@Valid @RequestBody CustomerLockForm lockForm) {
        log.info("Blacklist Customer Account by customer id: {}", lockForm.customerId());
        customerApiService.unlockAccount(lockForm);
        return ResponseEntity.noContent().build();
    }
}
