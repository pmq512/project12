package com.example.luxurystay.service.impl;

import com.example.luxurystay.dto.response.CityResponse;
import com.example.luxurystay.entity.City;
import com.example.luxurystay.repository.CityRepository;
import com.example.luxurystay.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<CityResponse> getAllCities() {
        return cityRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<CityResponse> getHotCities() {
        return cityRepository.findByIsHotTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<CityResponse> getCitiesByRegion(String region) {
        return cityRepository.findByRegion(region).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CityResponse getCityById(Long id) {
        return cityRepository.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    public CityResponse getCityByName(String name) {
        return cityRepository.findByName(name)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    public Page<CityResponse> getAllCitiesPaged(Pageable pageable) {
        return cityRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public Page<CityResponse> getHotCitiesPaged(Pageable pageable) {
        return cityRepository.findByIsHotTrue(pageable).map(this::toResponse);
    }

    @Override
    public Page<CityResponse> getCitiesByRegionPaged(String region, Pageable pageable) {
        return cityRepository.findByRegion(region, pageable).map(this::toResponse);
    }

    @Override
    public long countAllCities() {
        return cityRepository.count();
    }

    @Override
    public long countHotCities() {
        return cityRepository.findByIsHotTrue().size();
    }

    @Override
    public long countCitiesByRegion(String region) {
        return cityRepository.findByRegion(region).size();
    }

    private CityResponse toResponse(City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .province(city.getProvince())
                .region(city.getRegion())
                .isHot(city.getIsHot())
                .hotelCount(city.getHotelCount())
                .rating(city.getRating() != null ? city.getRating() : 4.5)
                .description(city.getDescription() != null ? city.getDescription() : "欢迎来到" + city.getName() + "，探索这座美丽的城市")
                .build();
    }
}