package com.example.luxurystay.controller;

import com.example.luxurystay.dto.request.HotelRequest;
import com.example.luxurystay.dto.response.ApiResponse;
import com.example.luxurystay.dto.response.HotelResponse;
import com.example.luxurystay.dto.response.ReviewResponse;
import com.example.luxurystay.service.HotelService;
import com.example.luxurystay.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final ReviewService reviewService;

    @GetMapping
    public String getAllHotels(Model model) {
        List<HotelResponse> hotels = hotelService.getAllHotels();
        model.addAttribute("hotels", hotels);
        return "hotels";
    }

    @GetMapping("/{id}")
    public String getHotelDetail(@PathVariable Long id, Model model) {
        HotelResponse hotel = hotelService.getHotelById(id);
        List<ReviewResponse> reviews = reviewService.getApprovedReviewsByHotel(id);
        long reviewCount = reviews.size();
        double avgRating = reviews.isEmpty() ? 0.0 : 
            reviews.stream().mapToInt(ReviewResponse::getRating).average().orElse(0.0);
        
        model.addAttribute("hotel", hotel);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("avgRating", Math.round(avgRating * 10) / 10.0);
        return "hotel-detail";
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HotelResponse>>> searchHotels(
            @RequestParam(required = false) String location) {
        List<HotelResponse> hotels = hotelService.searchHotels(location);
        return ResponseEntity.ok(ApiResponse.success(hotels));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<HotelResponse>>> getFeaturedHotels() {
        List<HotelResponse> hotels = hotelService.getFeaturedHotels();
        return ResponseEntity.ok(ApiResponse.success(hotels));
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<ApiResponse<List<HotelResponse>>> getBusinessHotels(@PathVariable Long businessId) {
        List<HotelResponse> hotels = hotelService.getHotelsByBusiness(businessId);
        return ResponseEntity.ok(ApiResponse.success(hotels));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HotelResponse>> createHotel(
            @Valid @RequestBody HotelRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        HotelResponse hotel = hotelService.createHotel(request, userId);
        return ResponseEntity.ok(ApiResponse.success("酒店创建成功", hotel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HotelResponse>> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelRequest request) {
        HotelResponse hotel = hotelService.updateHotel(id, request);
        return ResponseEntity.ok(ApiResponse.success("酒店更新成功", hotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
