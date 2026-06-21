package com.example.luxurystay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long id;
    private Long userId;
    private Long hotelId;
    private String hotelName;
    private String hotelImage;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer adults;
    private Integer children;
    private String roomType;
    private Integer totalPrice;
    private String status;
    private String bookingNumber;
    private Integer depositAmount;
    private Integer commissionAmount;
    private Integer businessIncome;
    private String guestName;
    private String phone;
    private String paymentMethod;
    private LocalDateTime createdAt;
}
