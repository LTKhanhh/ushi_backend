package com.example.ushi_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "districts")
@Getter
@Setter
@NoArgsConstructor
public class DistrictEntity {

    @Id
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity province;

    @JsonIgnore
    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    private List<AccountAddressEntity> accountAddresses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
    private List<PropertyAddressEntity> propertyAddresses = new ArrayList<>();
}
