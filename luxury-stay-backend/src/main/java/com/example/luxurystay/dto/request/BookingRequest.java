package com.example.luxurystay.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotNull(message = "酒店ID不能为空")
    @Positive(message = "酒店ID必须为正整数")
    private Long hotelId;

    @NotNull(message = "入住日期不能为空")
    private LocalDate checkIn;

    @NotNull(message = "退房日期不能为空")
    private LocalDate checkOut;

    @NotNull(message = "成人数量不能为空")
    @Positive(message = "成人数量必须为正整数")
    private Integer adults;

    private Integer children;

    @NotNull(message = "房型不能为空")
    private String roomType;
}