package com.example.luxurystay.repository;

import com.example.luxurystay.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    long countByUserId(Long userId);
    @Transactional
    void deleteByUserIdAndHotelId(Long userId, Long hotelId);
    Optional<Wishlist> findByUserIdAndHotelId(Long userId, Long hotelId);
}