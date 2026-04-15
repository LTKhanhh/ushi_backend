package com.example.ushi_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "account_addresses")
@Getter
@Setter
@NoArgsConstructor
public class AccountAddressEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String street;

    @Column(name = "details", length = 500)
    private String details;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    private DistrictEntity district;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private LandlordEntity landlord = new LandlordEntity();

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private UserEntity user = new UserEntity();
}
