package org.marketplace_lea.common.client.no_wallet.service.impl;

import org.marketplace_lea.common.client.no_wallet.config.NoWalletApiPropertiesConfig;
import org.marketplace_lea.common.client.no_wallet.dto.MobileOperator;
import org.marketplace_lea.common.client.no_wallet.service.OperatorTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertiesOperatorTokenProvider implements OperatorTokenProvider {
    private final NoWalletApiPropertiesConfig properties;
    
    @Override
    public String getTokenForOperator(String operator) {
        return switch (operator.toUpperCase()) {
            case "OM" -> properties.getOmToken();
            case "MTN" -> properties.getMtnToken();
            case "MOOV" -> properties.getMoovToken();
            default -> null;
        };
    }
    
    @Override
    public boolean supportsOperator(String operator) {
        String op = operator.toUpperCase();
        return Arrays.stream(MobileOperator.values())
                .anyMatch(mobileOperator -> op.equals(mobileOperator.name()));
    }

    @Override
    public String extractOperator(List<String> operatorsCode) {
        return (operatorsCode != null && !operatorsCode.isEmpty())
                ? operatorsCode.get(0).toUpperCase()
                : null;
    }
}