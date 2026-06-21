package com.example.luxurystay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private Integer adults;

    @Column(nullable = false)
    private Integer children;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    // 订单编号
    @Column(name = "booking_number", unique = true)
    private String bookingNumber;

    // 定金金额
    @Column(name = "deposit_amount")
    private Integer depositAmount;

    // 平台抽成金额（管理员抽成）
    @Column(name = "commission_amount")
    private Integer commissionAmount;

    // 商家实际收入（扣除抽成后）
    @Column(name = "business_income")
    private Integer businessIncome;

    // 联系人信息
    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "id_card")
    private String idCard;

    @Column(name = "special_requests", length = 500)
    private String specialRequests;

    @Column(name = "payment_method")
    private String paymentMethod;

    // 总客人数（用于兼容旧字段）
    @Column(name = "guests")
    private Integer guests;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 取消相关字段
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Transient
    private String hotelName;

    @Transient
    private String hotelImage;

    @Transient
    private String hotelLocation;

    @Column(name = "refund_amount")
    private Integer refundAmount;

    @Column(name = "refund_status")
    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = BookingStatus.PENDING;
        if (refundStatus == null) refundStatus = RefundStatus.NONE;
        if (bookingNumber == null) bookingNumber = "BK" + System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
