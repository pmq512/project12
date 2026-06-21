package com.example.luxurystay.service;

import com.example.luxurystay.dto.request.ReviewRequest;
import com.example.luxurystay.dto.response.ReviewResponse;
import com.example.luxurystay.entity.Hotel;
import com.example.luxurystay.entity.Review;
import com.example.luxurystay.entity.ReviewStatus;
import com.example.luxurystay.entity.User;
import com.example.luxurystay.repository.HotelRepository;
import com.example.luxurystay.repository.ReviewRepository;
import com.example.luxurystay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    public List<ReviewResponse> getReviewsByHotel(Long hotelId) {
        return reviewRepository.findByHotelIdAndStatus(hotelId, ReviewStatus.APPROVED).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<ReviewResponse> getApprovedReviewsByHotel(Long hotelId) {
        return reviewRepository.findByHotelIdAndStatus(hotelId, ReviewStatus.APPROVED).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<ReviewResponse> getPendingReviews() {
        return reviewRepository.findByStatus(ReviewStatus.PENDING).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<ReviewResponse> getPendingReviewsByBusiness(Long businessId) {
        List<Hotel> hotels = hotelRepository.findByBusinessId(businessId);
        return hotels.stream()
                .flatMap(hotel -> reviewRepository.findByHotelIdAndStatus(hotel.getId(), ReviewStatus.PENDING).stream())
                .map(this::convertToResponse)
                .toList();
    }

    public List<ReviewResponse> getApprovedReviewsByBusiness(Long businessId) {
        List<Hotel> hotels = hotelRepository.findByBusinessId(businessId);
        return hotels.stream()
                .flatMap(hotel -> reviewRepository.findByHotelIdAndStatus(hotel.getId(), ReviewStatus.APPROVED).stream())
                .map(this::convertToResponse)
                .toList();
    }

    public List<ReviewResponse> getRejectedReviewsByBusiness(Long businessId) {
        List<Hotel> hotels = hotelRepository.findByBusinessId(businessId);
        return hotels.stream()
                .flatMap(hotel -> reviewRepository.findByHotelIdAndStatus(hotel.getId(), ReviewStatus.REJECTED).stream())
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    public ReviewResponse createReview(ReviewRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        String title = request.getTitle();
        if (title == null || title.trim().isEmpty()) {
            title = "用户评价";
        }

        Review review = Review.builder()
                .hotelId(request.getHotelId())
                .userId(userId)
                .userName(user.getName())
                .rating(request.getRating())
                .title(title)
                .content(request.getContent())
                .status(ReviewStatus.PENDING)
                .build();

        Review savedReview = reviewRepository.save(review);

        updateHotelRating(request.getHotelId());

        return convertToResponse(savedReview);
    }

    @Transactional
    public ReviewResponse approveReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("评价不存在"));

        review.setStatus(ReviewStatus.APPROVED);
        review.setApprovedAt(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(review);

        updateHotelRating(review.getHotelId());

        return convertToResponse(updatedReview);
    }

    @Transactional
    public ReviewResponse rejectReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("评价不存在"));

        review.setStatus(ReviewStatus.REJECTED);
        review.setRejectedAt(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(review);

        updateHotelRating(review.getHotelId());

        return convertToResponse(updatedReview);
    }

    @Transactional
    public ReviewResponse replyToReview(Long id, String replyContent) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("评价不存在"));

        review.setBusinessReply(replyContent);
        review.setReplyAt(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(review);

        return convertToResponse(updatedReview);
    }

    private void updateHotelRating(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel != null) {
            List<Review> approvedReviews = reviewRepository.findByHotelIdAndStatus(hotelId, ReviewStatus.APPROVED);
            if (!approvedReviews.isEmpty()) {
                double avgRating = approvedReviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);
                hotel.setRating(Math.round(avgRating * 10) / 10.0);
                hotelRepository.save(hotel);
            }
        }
    }
    
    public List<ReviewResponse> getReviewsByUserIdOrdered(Long userId) {
        return reviewRepository.getReviewsByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    private ReviewResponse convertToResponse(Review review) {
        Hotel hotel = hotelRepository.findById(review.getHotelId()).orElse(null);

        return ReviewResponse.builder()
                .id(review.getId())
                .hotelId(review.getHotelId())
                .hotelName(hotel != null ? hotel.getName() : "未知酒店")
                .userId(review.getUserId())
                .userName(review.getUserName())
                .rating(review.getRating())
                .title(review.getTitle())
                .content(review.getContent())
                .businessReply(review.getBusinessReply())
                .replyAt(review.getReplyAt())
                .status(review.getStatus().name())
                .createdAt(review.getCreatedAt())
                .approvedAt(review.getApprovedAt())
                .rejectedAt(review.getRejectedAt())
                .build();
    }
}