package com.example.luxurystay.repository;

import com.example.luxurystay.entity.DisputeStatus;
import com.example.luxurystay.entity.OrderDispute;
import com.example.luxurystay.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDisputeRepository extends JpaRepository<OrderDispute, Long> {
    
    List<OrderDispute> findByStatus(DisputeStatus status);
    
    List<OrderDispute> findByApplicantIdAndApplicantType(Long applicantId, UserType applicantType);
    
    List<OrderDispute> findByBookingId(Long bookingId);
    
    List<OrderDispute> findByAdminId(Long adminId);
    
    long countByStatus(DisputeStatus status);
    
    @org.springframework.data.jpa.repository.Query("SELECT d FROM OrderDispute d JOIN Booking b ON d.bookingId = b.id JOIN Hotel h ON b.hotelId = h.id WHERE h.businessId = :businessId")
    List<OrderDispute> findByBusinessId(@org.springframework.data.repository.query.Param("businessId") Long businessId);
    
    @org.springframework.data.jpa.repository.Query("SELECT COUNT(d) FROM OrderDispute d JOIN Booking b ON d.bookingId = b.id JOIN Hotel h ON b.hotelId = h.id WHERE h.businessId = :businessId AND d.status = :status")
    long countByBusinessIdAndStatus(@org.springframework.data.repository.query.Param("businessId") Long businessId, @org.springframework.data.repository.query.Param("status") DisputeStatus status);
}
