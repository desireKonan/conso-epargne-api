package org.marketplace_lea.common.client.no_wallet.service.impl;

import org.marketplace_lea.common.client.no_wallet.NoWalletClient;
import org.marketplace_lea.common.client.no_wallet.exception.NoWalletException;
import org.marketplace_lea.common.client.no_wallet.form.InitiatePaymentNoWalletForm;
import org.marketplace_lea.common.client.no_wallet.form.NoWalletForm;
import org.marketplace_lea.common.client.no_wallet.response.NoWalletPaymentResponse;
import org.marketplace_lea.common.client.no_wallet.service.NoWalletPaymentService;
import org.marketplace_lea.common.client.no_wallet.service.OperatorTokenProvider;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultNoWalletPaymentService implements NoWalletPaymentService {
    private final NoWalletClient noWalletClient;
    private final OperatorTokenProvider tokenProvider;

    @Override
    public NoWalletPaymentResponse processPayment(String transactionId, NoWalletForm noWalletForm) {
        String operator = tokenProvider.extractOperator(noWalletForm.getOperatorsCode());
        log.info("[DefaultNoWalletPaymentService][processPayment] Extract operator: {}", operator);

        if (!tokenProvider.supportsOperator(operator)) {
            throw new NoWalletException("Opérateur non supporté !", HttpStatus.BAD_REQUEST);
        }

        String bearerToken = tokenProvider.getTokenForOperator(operator);
        log.info("[DefaultNoWalletPaymentService][processPayment] get correct bearer token: {}", bearerToken.substring(0, 8));

        String formatBearerToken = GeneratorUtils.bearerToken(bearerToken);

        var request = InitiatePaymentNoWalletForm.builder()
                .transactionId(transactionId)
                .countryCode(noWalletForm.getCountryCode())
                .operatorsCode(noWalletForm.getOperatorsCode())
                .operatorOtp(noWalletForm.getOperatorOtp())
                .method(noWalletForm.getMethod())
                .amount(noWalletForm.getAmount())
                .additionalInfos(noWalletForm.getAdditionalInfos())
                .callbackUrl(noWalletForm.getCallbackUrl())
                .returnUrl(noWalletForm.getReturnUrl())
                .tunnel(noWalletForm.getTunnel())
                .build();
        log.info("[DefaultNoWalletPaymentService][processPayment] init payment form: {}", request);

        try {
            NoWalletPaymentResponse response = noWalletClient.initiatePayment(formatBearerToken, request);
            log.info("NoWallet API response - status: {}", response.statusPayment());
            return response;
        } catch (NoWalletException e) {
            log.error("Error calling NoWallet API", e);
            throw e;
        }
    }
}
