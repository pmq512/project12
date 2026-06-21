package com.example.luxurystay.service;

import com.example.luxurystay.dto.request.BookingRequest;
import com.example.luxurystay.dto.response.BookingResponse;
import com.example.luxurystay.entity.Booking;
import com.example.luxurystay.entity.BookingStatus;
import com.example.luxurystay.entity.Hotel;
import com.example.luxurystay.entity.RefundStatus;
import com.example.luxurystay.entity.User;
import com.example.luxurystay.repository.BookingRepository;
import com.example.luxurystay.repository.HotelRepository;
import com.example.luxurystay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    public List<BookingResponse> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<BookingResponse> getBookingsByUserIdAndStatus(Long userId, BookingStatus status) {
        return bookingRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<BookingResponse> getBookingsByHotel(Long hotelId) {
        return bookingRepository.findByHotelId(hotelId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<BookingResponse> getBookingsByBusiness(Long businessId) {
        return bookingRepository.findByBusinessId(businessId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    public long countBookingsByBusiness(Long businessId) {
        return bookingRepository.countByBusinessId(businessId);
    }

    public long countMonthlyBookingsByBusiness(Long businessId) {
        return bookingRepository.countMonthlyByBusinessId(businessId);
    }

    public long countPendingBookingsByBusiness(Long businessId) {
        return bookingRepository.countPendingByBusinessId(businessId);
    }

    public BigDecimal sumRevenueByBusiness(Long businessId) {
        Integer total = bookingRepository.sumTotalPriceByBusinessId(businessId);
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    public List<BookingResponse> getRecentBookingsByBusiness(Long businessId) {
        return bookingRepository.findRecentByBusinessId(businessId).stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    public void completeBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预订不存在"));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("只能完成已确认的订单");
        }

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        int totalPrice = booking.getTotalPrice();
        int commissionRate = (hotel != null && hotel.getCommissionRate() != null) ? hotel.getCommissionRate() : 10;
        int commissionAmount = totalPrice * commissionRate / 100;
        int businessIncome = totalPrice - commissionAmount;

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCommissionAmount(commissionAmount);
        booking.setBusinessIncome(businessIncome);
        bookingRepository.save(booking);
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request, Long userId) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new IllegalArgumentException("酒店不存在"));

        if (request.getCheckIn().isAfter(request.getCheckOut())) {
            throw new IllegalArgumentException("入住日期不能晚于退房日期");
        }

        long days = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
        int totalPrice = hotel.getPrice() * (int) days;
        int depositAmount = hotel.getDepositAmount() != null ? hotel.getDepositAmount() : 0;

        Booking booking = Booking.builder()
                .userId(userId)
                .hotelId(request.getHotelId())
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .adults(request.getAdults())
                .children(request.getChildren() != null ? request.getChildren() : 0)
                .roomType(request.getRoomType())
                .totalPrice(totalPrice)
                .depositAmount(depositAmount)
                .status(BookingStatus.CONFIRMED)
                .build();

        Hotel updatedHotel = hotelRepository.findById(request.getHotelId()).orElse(null);
        if (updatedHotel != null && updatedHotel.getRoomsAvailable() > 0) {
            updatedHotel.setRoomsAvailable(updatedHotel.getRoomsAvailable() - 1);
            hotelRepository.save(updatedHotel);
        }

        Booking savedBooking = bookingRepository.save(booking);
        return convertToResponse(savedBooking);
    }

    @Transactional
    public void cancelBooking(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预订不存在"));

        if (!booking.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权取消此预订");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        if (hotel != null) {
            hotel.setRoomsAvailable(hotel.getRoomsAvailable() + 1);
            hotelRepository.save(hotel);
        }
    }

    @Transactional
    public void confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预订不存在"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalArgumentException("只能确认待处理状态的订单");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    @Transactional
    public void businessCancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预订不存在"));

        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.REFUNDED) {
            throw new IllegalArgumentException("当前状态不允许取消");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        if (hotel != null) {
            hotel.setRoomsAvailable(hotel.getRoomsAvailable() + 1);
            hotelRepository.save(hotel);
        }
    }

    @Transactional
    public void refundBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预订不存在"));

        if (booking.getStatus() != BookingStatus.CONFIRMED && booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalArgumentException("只能退已确认的订单");
        }

        booking.setStatus(BookingStatus.REFUNDED);
        bookingRepository.save(booking);

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        if (hotel != null) {
            hotel.setRoomsAvailable(hotel.getRoomsAvailable() + 1);
            hotelRepository.save(hotel);
        }
    }

    @Transactional
    public void userCancelBooking(Long id, Long userId, String reason) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预订不存在"));

        if (!booking.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权取消此预订");
        }

        int refundAmount = calculateRefund(booking);
        
        booking.setStatus(BookingStatus.USER_CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);
        booking.setRefundAmount(refundAmount);
        booking.setRefundStatus(refundAmount > 0 ? RefundStatus.PENDING : RefundStatus.NONE);
        bookingRepository.save(booking);

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        if (hotel != null) {
            hotel.setRoomsAvailable(hotel.getRoomsAvailable() + 1);
            hotelRepository.save(hotel);
        }
    }

    @Transactional
    public void markNoShow(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预订不存在"));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("只能标记已确认订单为未入住");
        }

        if (!LocalDate.now().isAfter(booking.getCheckIn())) {
            throw new IllegalArgumentException("还未到入住日期，不能标记为未入住");
        }

        booking.setStatus(BookingStatus.NO_SHOW);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason("用户未入住");
        booking.setRefundAmount(0);
        booking.setRefundStatus(RefundStatus.NONE);
        bookingRepository.save(booking);

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        if (hotel != null) {
            hotel.setRoomsAvailable(hotel.getRoomsAvailable() + 1);
            hotelRepository.save(hotel);
        }
    }

    @Transactional
    public void handleUserCancellation(Long id, String reason) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预订不存在"));

        int refundAmount = calculateRefund(booking);
        
        booking.setStatus(BookingStatus.USER_CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);
        booking.setRefundAmount(refundAmount);
        booking.setRefundStatus(refundAmount > 0 ? RefundStatus.PENDING : RefundStatus.NONE);
        bookingRepository.save(booking);

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        if (hotel != null) {
            hotel.setRoomsAvailable(hotel.getRoomsAvailable() + 1);
            hotelRepository.save(hotel);
        }
    }

    private int calculateRefund(Booking booking) {
        if (booking.getStatus() == BookingStatus.PENDING) {
            return booking.getTotalPrice();
        }

        long daysBeforeCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), booking.getCheckIn());
        
        if (daysBeforeCheckIn >= 7) {
            return booking.getTotalPrice();
        } else if (daysBeforeCheckIn >= 3) {
            return booking.getTotalPrice() * 70 / 100;
        } else if (daysBeforeCheckIn >= 1) {
            return booking.getTotalPrice() * 50 / 100;
        } else {
            return 0;
        }
    }

    @Transactional
    public void confirmRefund(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预订不存在"));

        if (booking.getRefundStatus() != RefundStatus.PENDING) {
            throw new IllegalArgumentException("当前状态不允许退款");
        }

        Integer refund = booking.getRefundAmount();
        Integer total = booking.getTotalPrice();
        booking.setRefundStatus(refund != null && refund.equals(total) 
                ? RefundStatus.FULL : RefundStatus.PARTIAL);
        booking.setStatus(BookingStatus.REFUNDED);
        bookingRepository.save(booking);
    }

    @Transactional
    public void rejectRefund(Long id, String reason) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("预订不存在"));

        if (booking.getRefundStatus() != RefundStatus.PENDING) {
            throw new IllegalArgumentException("当前状态不允许拒绝退款");
        }

        booking.setRefundStatus(RefundStatus.REJECTED);
        booking.setCancellationReason(reason);
        bookingRepository.save(booking);
    }

    public BigDecimal sumBusinessIncomeByBusiness(Long businessId) {
        Integer total = bookingRepository.sumBusinessIncomeByBusinessId(businessId);
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    public BigDecimal sumCommissionByBusiness(Long businessId) {
        Integer total = bookingRepository.sumCommissionByBusinessId(businessId);
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    public BigDecimal sumTotalCommission() {
        Long total = bookingRepository.sumTotalCommission();
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    public BigDecimal sumTotalBusinessIncome() {
        Long total = bookingRepository.sumTotalBusinessIncome();
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    public BigDecimal sumTotalRevenue() {
        Long total = bookingRepository.sumTotalRevenue();
        return total != null ? BigDecimal.valueOf(total) : BigDecimal.ZERO;
    }

    private BookingResponse convertToResponse(Booking booking) {
        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        User user = userRepository.findById(booking.getUserId()).orElse(null);
        
        String guestName = booking.getGuestName();
        if (guestName == null || guestName.isEmpty()) {
            guestName = user != null ? user.getName() : "未知用户";
        }

        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .hotelId(booking.getHotelId())
                .hotelName(hotel != null ? hotel.getName() : "未知酒店")
                .hotelImage(hotel != null ? hotel.getImage() : "")
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .adults(booking.getAdults())
                .children(booking.getChildren())
                .roomType(booking.getRoomType())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().name())
                .bookingNumber(booking.getBookingNumber())
                .depositAmount(booking.getDepositAmount())
                .commissionAmount(booking.getCommissionAmount())
                .businessIncome(booking.getBusinessIncome())
                .guestName(guestName)
                .phone(booking.getPhone())
                .paymentMethod(booking.getPaymentMethod())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}