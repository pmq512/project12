package com.example.luxurystay.service;

import com.example.luxurystay.dto.response.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityService {

    List<CityResponse> getAllCities();
    
    List<CityResponse> getHotCities();
    
    List<CityResponse> getCitiesByRegion(String region);
    
    CityResponse getCityById(Long id);
    
    CityResponse getCityByName(String name);
    
    Page<CityResponse> getAllCitiesPaged(Pageable pageable);
    
    Page<CityResponse> getHotCitiesPaged(Pageable pageable);
    
    Page<CityResponse> getCitiesByRegionPaged(String region, Pageable pageable);
    
    long countAllCities();
    
    long countHotCities();
    
    long countCitiesByRegion(String region);
}
