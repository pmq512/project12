package com.example.luxurystay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "hotels")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Integer star;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Integer price;

    @Column(columnDefinition = "TEXT")
    private String facilities;

    @Column(name = "review_count")
    @Builder.Default
    private Integer reviewCount = 0;

    @Column(name = "rooms_available", nullable = false)
    private Integer roomsAvailable;

    @Column(name = "business_id")
    private Long businessId;

    // 定金与逾期政策
    @Column(name = "deposit_amount")
    private Integer depositAmount;

    @Column(name = "deposit_policy", length = 500)
    private String depositPolicy;

    @Column(name = "no_show_policy", length = 500)
    private String noShowPolicy;

    // 平台抽成比例（默认10%）
    @Builder.Default
    @Column(name = "commission_rate")
    private Integer commissionRate = 10;

    // 酒店状态：ACTIVE上架, INACTIVE下架
    @Builder.Default
    @Column(nullable = false)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = "ACTIVE";
        if (commissionRate == null) commissionRate = 10;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
