package com.example.luxurystay.repository;

import com.example.luxurystay.entity.Review;
import com.example.luxurystay.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByHotelId(Long hotelId);
    List<Review> findByHotelIdAndStatus(Long hotelId, ReviewStatus status);
    List<Review> findByUserId(Long userId);
    List<Review> findByStatus(ReviewStatus status);
    List<Review> findTop4ByStatusOrderByCreatedAtDesc(ReviewStatus status);
    
    long countByUserId(Long userId);
    
    @Query("SELECT r FROM Review r WHERE r.userId = :userId ORDER BY r.createdAt DESC")
    List<Review> getReviewsByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    long countByStatus(ReviewStatus status);

    @Query("SELECT r FROM Review r WHERE r.userId = :userId ORDER BY r.createdAt DESC")
    List<Review> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
