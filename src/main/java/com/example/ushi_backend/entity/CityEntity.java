package com.example.ushi_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
public class CityEntity {
    @Id
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    private List<DistrictEntity> districts = new ArrayList<>();
}
