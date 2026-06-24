package com.example.luxurystay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityResponse {

    private Long id;
    private String name;
    private String province;
    private String region;
    private Boolean isHot;
    private Integer hotelCount;
    private Double rating;
    private String description;
    private String image;
}
