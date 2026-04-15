package com.example.ushi_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "landlords",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class LandlordEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String phone;

    @Column(length = 500)
    private String avatar;

    @JsonIgnore
    @OneToOne()
    @JoinColumn(name = "address_id",nullable = true)
    private AccountAddressEntity address;

    @JsonIgnore
    @OneToMany(mappedBy = "landlord", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PropertyEntity> properties = new ArrayList<>();
}
