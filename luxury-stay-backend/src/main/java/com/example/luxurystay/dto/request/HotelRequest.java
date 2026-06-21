package com.example.luxurystay.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {

    @NotBlank(message = "酒店名称不能为空")
    private String name;

    @NotBlank(message = "位置不能为空")
    private String location;

    private String description;

    @NotBlank(message = "图片URL不能为空")
    private String image;

    @NotNull(message = "星级不能为空")
    @Positive(message = "星级必须为正整数")
    private Integer star;

    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须为正整数")
    private Integer price;

    private String facilities;

    @NotNull(message = "可用房间数不能为空")
    @Positive(message = "可用房间数必须为正整数")
    private Integer roomsAvailable;

    private Integer depositAmount;

    private String depositPolicy;

    private String noShowPolicy;

    private Integer commissionRate;
}