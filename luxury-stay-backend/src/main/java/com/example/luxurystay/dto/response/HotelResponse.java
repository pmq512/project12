package com.example.luxurystay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {

    private Long id;
    private String name;
    private String location;
    private String description;
    private String image;
    private Integer star;
    private Double rating;
    private Integer price;
    private String facilities;
    private Integer roomsAvailable;
    private Long businessId;

    private Integer depositAmount;
    private String depositPolicy;
    private String noShowPolicy;
    private Integer commissionRate;
    private String status;
}
