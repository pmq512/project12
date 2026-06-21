package com.example.luxurystay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cities")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "province")
    private String province;

    @Column(name = "region")
    private String region;

    @Column(name = "is_hot", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isHot;

    @Column(name = "hotel_count", columnDefinition = "INTEGER DEFAULT 0")
    private Integer hotelCount;
    
    @Column(name = "rating", columnDefinition = "DOUBLE DEFAULT 4.5")
    private Double rating;
    
    @Column(name = "description", length = 500)
    private String description;
}