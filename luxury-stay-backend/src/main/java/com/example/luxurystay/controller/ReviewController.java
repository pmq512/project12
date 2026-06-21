package com.example.luxurystay.controller;

import com.example.luxurystay.dto.request.ReviewReplyRequest;
import com.example.luxurystay.dto.request.ReviewRequest;
import com.example.luxurystay.dto.response.ApiResponse;
import com.example.luxurystay.dto.response.ReviewResponse;
import com.example.luxurystay.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getHotelReviews(@PathVariable Long hotelId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByHotel(hotelId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getPendingReviews() {
        List<ReviewResponse> reviews = reviewService.getPendingReviews();
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @GetMapping("/pending/business/{businessId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getBusinessPendingReviews(@PathVariable Long businessId) {
        List<ReviewResponse> reviews = reviewService.getPendingReviewsByBusiness(businessId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody ReviewRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        ReviewResponse review = reviewService.createReview(request, userId);
        return ResponseEntity.ok(ApiResponse.success("评价提交成功，待审核", review));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<ReviewResponse>> approveReview(@PathVariable Long id) {
        ReviewResponse review = reviewService.approveReview(id);
        return ResponseEntity.ok(ApiResponse.success("评价已通过", review));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ReviewResponse>> rejectReview(@PathVariable Long id) {
        ReviewResponse review = reviewService.rejectReview(id);
        return ResponseEntity.ok(ApiResponse.success("评价已拒绝", review));
    }

    @PutMapping("/{id}/reply")
    public ResponseEntity<ApiResponse<ReviewResponse>> replyToReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewReplyRequest request) {
        ReviewResponse review = reviewService.replyToReview(id, request.getReplyContent());
        return ResponseEntity.ok(ApiResponse.success("回复成功", review));
    }
}