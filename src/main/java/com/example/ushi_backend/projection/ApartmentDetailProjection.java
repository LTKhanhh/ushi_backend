package com.example.ushi_backend.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ApartmentDetailProjection {
    Long getPostId();

    String getName();

    String getAddress();

    BigDecimal getPrice();

    String getPackageType();

    Integer getLiked();

    String getThumbnailImageUrl();

    String getDescription();

    String getPropertyType();

    BigDecimal getArea();

    Integer getBedrooms();

    Integer getBathrooms();

    Integer getMaxPeople();

    String getDepositText();

    String getPaymentCycle();

    LocalDate getPostedDate();

    Long getViewCount();

    Long getFavoriteCount();

    Boolean getAuth();

    BigDecimal getLatitude();

    BigDecimal getLongitude();

    String getCity();

    String getDistrict();

    String getStreet();

    String getAddressDetails();

    Long getLandlordId();

    String getLandlordName();

    String getLandlordPhone();

    String getLandlordAvatar();

    Long getLandlordActivePostCount();
}
