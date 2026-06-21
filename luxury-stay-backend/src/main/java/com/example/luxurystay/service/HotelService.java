package com.example.luxurystay.service;

import com.example.luxurystay.dto.request.HotelRequest;
import com.example.luxurystay.dto.response.HotelResponse;
import com.example.luxurystay.entity.Hotel;
import com.example.luxurystay.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<HotelResponse> getFeaturedHotels() {
        return hotelRepository.findTop6ByOrderByRatingDesc().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<HotelResponse> searchHotels(String location) {
        if (location == null || location.isEmpty()) {
            return getAllHotels();
        }
        return hotelRepository.findByLocationContaining(location).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public HotelResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("酒店不存在" + id));
        return convertToResponse(hotel);
    }

    @Transactional
    public HotelResponse createHotel(HotelRequest request, Long businessId) {
        Hotel hotel = Hotel.builder()
                .name(request.getName())
                .location(request.getLocation())
                .description(request.getDescription())
                .image(request.getImage())
                .star(request.getStar())
                .rating(0.0)
                .price(request.getPrice())
                .facilities(request.getFacilities())
                .roomsAvailable(request.getRoomsAvailable())
                .businessId(businessId)
                .depositAmount(request.getDepositAmount())
                .depositPolicy(request.getDepositPolicy())
                .noShowPolicy(request.getNoShowPolicy())
                .commissionRate(request.getCommissionRate() != null ? request.getCommissionRate() : 10)
                .status("ACTIVE")
                .build();

        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToResponse(savedHotel);
    }

    @Transactional
    public HotelResponse updateHotel(Long id, HotelRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("酒店不存在" + id));

        hotel.setName(request.getName());
        hotel.setLocation(request.getLocation());
        hotel.setDescription(request.getDescription());
        hotel.setImage(request.getImage());
        hotel.setStar(request.getStar());
        hotel.setPrice(request.getPrice());
        hotel.setFacilities(request.getFacilities());
        hotel.setRoomsAvailable(request.getRoomsAvailable());
        hotel.setDepositAmount(request.getDepositAmount());
        hotel.setDepositPolicy(request.getDepositPolicy());
        hotel.setNoShowPolicy(request.getNoShowPolicy());
        if (request.getCommissionRate() != null) {
            hotel.setCommissionRate(request.getCommissionRate());
        }

        Hotel updatedHotel = hotelRepository.save(hotel);
        return convertToResponse(updatedHotel);
    }

    @Transactional
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new IllegalArgumentException("酒店不存在" + id);
        }
        hotelRepository.deleteById(id);
    }

    public List<HotelResponse> getHotelsByBusiness(Long businessId) {
        return hotelRepository.findByBusinessId(businessId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    private HotelResponse convertToResponse(Hotel hotel) {
        return HotelResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .location(hotel.getLocation())
                .description(hotel.getDescription())
                .image(hotel.getImage())
                .star(hotel.getStar())
                .rating(hotel.getRating())
                .price(hotel.getPrice())
                .facilities(hotel.getFacilities())
                .roomsAvailable(hotel.getRoomsAvailable())
                .businessId(hotel.getBusinessId())
                .depositAmount(hotel.getDepositAmount())
                .depositPolicy(hotel.getDepositPolicy())
                .noShowPolicy(hotel.getNoShowPolicy())
                .commissionRate(hotel.getCommissionRate())
                .status(hotel.getStatus())
                .build();
    }
}
