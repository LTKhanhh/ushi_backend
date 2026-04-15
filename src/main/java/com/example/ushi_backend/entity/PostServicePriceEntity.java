package com.example.ushi_backend.entity;

import com.example.ushi_backend.entity.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "post_service_prices",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"post_id", "service_type"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class PostServicePriceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 loại dịch vụ (fix cứng bằng enum)
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false, length = 30)
    private ServiceType serviceType;

    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unit_label", length = 50)
    private String unitLabel;

    @Column(length = 255)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;
}