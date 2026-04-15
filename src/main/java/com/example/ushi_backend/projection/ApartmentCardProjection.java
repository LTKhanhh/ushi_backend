package com.example.ushi_backend.projection;

import java.math.BigDecimal;

public interface ApartmentCardProjection {
    Long getPostId();

    String getName();

    String getAddress();

    BigDecimal getPrice();

    String getPackageType();

    Integer getLiked();

    String getThumbnailImageUrl();
}
