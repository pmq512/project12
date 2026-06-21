package com.example.luxurystay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private Long hotelId;
    private String hotelName;
    private Long userId;
    private String userName;
    private Integer rating;
    private String title;
    private String content;
    private String businessReply;
    private LocalDateTime replyAt;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
}
