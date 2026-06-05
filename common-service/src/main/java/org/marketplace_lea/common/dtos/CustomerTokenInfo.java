package org.marketplace_lea.common.dtos;

import java.math.BigDecimal;
import java.util.List;

public record CustomerTokenInfo(
        String id,
        AccountTokenInfo account,
        String firstName,
        String lastName,
        String email,
        List<String> phoneNumbers,
        String address,
        BigDecimal longitude,
        BigDecimal latitude
){}