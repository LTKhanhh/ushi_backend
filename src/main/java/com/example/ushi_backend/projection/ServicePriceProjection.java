package com.example.ushi_backend.projection;

import java.math.BigDecimal;

public interface ServicePriceProjection {
    String getServiceType();

    BigDecimal getUnitPrice();

    String getUnitLabel();

    String getNote();
}
