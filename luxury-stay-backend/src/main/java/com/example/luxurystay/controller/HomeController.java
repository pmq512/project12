package com.example.luxurystay.controller;

import com.example.luxurystay.dto.response.HotelResponse;
import com.example.luxurystay.dto.response.ReviewResponse;
import com.example.luxurystay.entity.ReviewStatus;
import com.example.luxurystay.repository.HotelRepository;
import com.example.luxurystay.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<HotelResponse> featuredHotels = hotelRepository.findTop6ByOrderByRatingDesc().stream()
                .map(hotel -> HotelResponse.builder()
                        .id(hotel.getId())
                        .name(hotel.getName())
                        .location(hotel.getLocation())
                        .image(hotel.getImage())
                        .star(hotel.getStar())
                        .rating(hotel.getRating())
                        .price(hotel.getPrice())
                        .build())
                .toList();

        List<ReviewResponse> reviews = reviewRepository.findTop4ByStatusOrderByCreatedAtDesc(ReviewStatus.APPROVED).stream()
                .map(review -> {
                    String hotelName = hotelRepository.findById(review.getHotelId())
                            .map(h -> h.getName())
                            .orElse("未知酒店");
                    return ReviewResponse.builder()
                            .id(review.getId())
                            .userName(review.getUserName())
                            .content(review.getContent())
                            .rating(review.getRating())
                            .hotelName(hotelName)
                            .createdAt(review.getCreatedAt())
                            .build();
                })
                .toList();

        model.addAttribute("featuredHotels", featuredHotels);
        model.addAttribute("reviews", reviews);
        return "index";
    }

    @GetMapping("/search")
    public String search(Model model, 
                        @RequestParam(required = false) String location,
                        @RequestParam(required = false) String checkIn,
                        @RequestParam(required = false) String checkOut,
                        @RequestParam(required = false) Integer adults,
                        @RequestParam(required = false) Integer children,
                        @RequestParam(required = false) Integer maxPrice,
                        @RequestParam(required = false) String amenities,
                        @RequestParam(required = false) String features) {
        
        List<HotelResponse> hotels;
        if (location != null && !location.isEmpty()) {
            String keyword = location.trim();
            hotels = hotelRepository.findAll().stream()
                    .filter(hotel -> 
                        hotel.getName().contains(keyword) || 
                        hotel.getLocation().contains(keyword)
                    )
                    .map(hotel -> HotelResponse.builder()
                            .id(hotel.getId())
                            .name(hotel.getName())
                            .location(hotel.getLocation())
                            .image(hotel.getImage())
                            .star(hotel.getStar())
                            .rating(hotel.getRating())
                            .price(hotel.getPrice())
                            .facilities(hotel.getFacilities())
                            .build())
                    .toList();
        } else {
            hotels = hotelRepository.findAll().stream()
                    .map(hotel -> HotelResponse.builder()
                            .id(hotel.getId())
                            .name(hotel.getName())
                            .location(hotel.getLocation())
                            .image(hotel.getImage())
                            .star(hotel.getStar())
                            .rating(hotel.getRating())
                            .price(hotel.getPrice())
                            .facilities(hotel.getFacilities())
                            .build())
                    .toList();
        }

        if (maxPrice != null) {
            hotels = hotels.stream()
                    .filter(h -> h.getPrice() <= maxPrice)
                    .toList();
        }

        if (amenities != null && !amenities.isEmpty()) {
            String[] amenityList = amenities.split(",");
            hotels = hotels.stream()
                    .filter(h -> {
                        if (h.getFacilities() == null) return false;
                        for (String amenity : amenityList) {
                            if (!h.getFacilities().contains(amenity.trim())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .toList();
        }

        if (features != null && !features.isEmpty()) {
            String[] featureList = features.split(",");
            hotels = hotels.stream()
                    .filter(h -> {
                        if (h.getFacilities() == null) return false;
                        for (String feature : featureList) {
                            if (!h.getFacilities().contains(feature.trim())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .toList();
        }

        model.addAttribute("hotels", hotels);
        model.addAttribute("location", location);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("adults", adults);
        model.addAttribute("children", children);
        model.addAttribute("maxPrice", maxPrice);
        return "search";
    }
}
