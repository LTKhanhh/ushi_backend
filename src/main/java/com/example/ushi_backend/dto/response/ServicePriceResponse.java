package com.example.ushi_backend.dto.response;

import java.math.BigDecimal;

public record ServicePriceResponse(
        String serviceType,
        BigDecimal unitPrice,
        String unitLabel,
        String note
) {
}
