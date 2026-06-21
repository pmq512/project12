package com.example.luxurystay.repository;

import com.example.luxurystay.entity.Booking;
import com.example.luxurystay.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByHotelId(Long hotelId);
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
    List<Booking> findByHotelIdAndStatus(Long hotelId, BookingStatus status);
    
    @Query("SELECT b FROM Booking b JOIN Hotel h ON b.hotelId = h.id WHERE h.businessId = :businessId")
    List<Booking> findByBusinessId(@Param("businessId") Long businessId);
    
    @Query("SELECT COUNT(b) FROM Booking b JOIN Hotel h ON b.hotelId = h.id WHERE h.businessId = :businessId")
    long countByBusinessId(@Param("businessId") Long businessId);
    
    @Query("SELECT COUNT(b) FROM Booking b JOIN Hotel h ON b.hotelId = h.id WHERE h.businessId = :businessId AND FUNCTION('strftime', '%Y-%m', b.createdAt) = FUNCTION('strftime', '%Y-%m', 'now')")
    long countMonthlyByBusinessId(@Param("businessId") Long businessId);
    
    @Query("SELECT COUNT(b) FROM Booking b JOIN Hotel h ON b.hotelId = h.id WHERE h.businessId = :businessId AND b.status = 'PENDING'")
    long countPendingByBusinessId(@Param("businessId") Long businessId);
    
    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b JOIN Hotel h ON b.hotelId = h.id WHERE h.businessId = :businessId AND b.status = 'COMPLETED'")
    Integer sumTotalPriceByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(SUM(b.businessIncome), 0) FROM Booking b JOIN Hotel h ON b.hotelId = h.id WHERE h.businessId = :businessId AND b.status = 'COMPLETED'")
    Integer sumBusinessIncomeByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(SUM(b.commissionAmount), 0) FROM Booking b JOIN Hotel h ON b.hotelId = h.id WHERE h.businessId = :businessId AND b.status = 'COMPLETED'")
    Integer sumCommissionByBusinessId(@Param("businessId") Long businessId);

    long countByUserId(Long userId);
    
    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b WHERE b.userId = :userId")
    Integer sumTotalPriceByUserId(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.userId = :userId AND b.status = :status ORDER BY b.checkIn DESC")
    List<Booking> findByUserIdAndStatusOrderByCheckInDesc(@Param("userId") Long userId, @Param("status") BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.userId = :userId ORDER BY b.createdAt DESC")
    List<Booking> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.userId = :userId AND b.status = :status ORDER BY b.checkOut DESC")
    List<Booking> findByUserIdAndStatusOrderByCheckOutDesc(@Param("userId") Long userId, @Param("status") BookingStatus status);
    
    @Query(value = "SELECT b.* FROM bookings b JOIN hotels h ON b.hotel_id = h.id WHERE h.business_id = :businessId ORDER BY b.created_at DESC LIMIT 10", nativeQuery = true)
    List<Booking> findRecentByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT COALESCE(SUM(b.commissionAmount), 0) FROM Booking b WHERE b.status = 'COMPLETED'")
    Long sumTotalCommission();

    @Query("SELECT COALESCE(SUM(b.businessIncome), 0) FROM Booking b WHERE b.status = 'COMPLETED'")
    Long sumTotalBusinessIncome();

    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b WHERE b.status = 'COMPLETED'")
    Long sumTotalRevenue();

    long countByStatus(BookingStatus status);
    long countByUserIdAndStatus(Long userId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.userId = :userId AND b.status = :status ORDER BY b.createdAt DESC")
    List<Booking> findByUserIdAndStatusOrderByCreatedAtDesc(@Param("userId") Long userId, @Param("status") BookingStatus status);
}
