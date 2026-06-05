package org.marketplace_lea.prometheus.domain.onboarding.controllers;

import org.marketplace_lea.common.common.constants.Router;
import org.marketplace_lea.common.dtos.CustomerV2DTO;
import org.marketplace_lea.common.forms.RegistrationV2Form;
import org.marketplace_lea.prometheus.domain.onboarding.services.CustomerOnboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Router.API_REGISTRATION_URI)
public class RegistrationApiV2Controller {
    private final CustomerOnboardingService onboardingService;

    @PostMapping
    public ResponseEntity<CustomerV2DTO> register(@Valid @RequestBody RegistrationV2Form form) {
        var customerRegistered = onboardingService.onboard(form);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerRegistered);
    }
}
