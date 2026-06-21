package com.example.luxurystay.repository;

import com.example.luxurystay.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByLocationContaining(String location);
    List<Hotel> findByBusinessId(Long businessId);
    List<Hotel> findTop6ByOrderByRatingDesc();
    List<Hotel> findAllByOrderByRatingDesc();
    List<Hotel> findByPriceBetween(Integer minPrice, Integer maxPrice);
    List<Hotel> findByStatus(String status);
    long countByStatus(String status);
}
