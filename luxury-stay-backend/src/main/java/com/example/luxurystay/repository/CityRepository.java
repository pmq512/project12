package com.example.luxurystay.repository;

import com.example.luxurystay.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    
    Optional<City> findByName(String name);
    
    List<City> findByIsHotTrue();
    
    List<City> findByRegion(String region);
    
    List<City> findByProvince(String province);
    
    Page<City> findAll(Pageable pageable);
    
    Page<City> findByIsHotTrue(Pageable pageable);
    
    Page<City> findByRegion(String region, Pageable pageable);
}
