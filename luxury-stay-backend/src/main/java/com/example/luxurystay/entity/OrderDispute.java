package com.example.luxurystay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_disputes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDispute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "applicant_id", nullable = false)
    private Long applicantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicant_type", nullable = false)
    private UserType applicantType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String evidence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisputeStatus status;

    @Column(columnDefinition = "TEXT")
    private String adminNote;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Transient
    private String userName;

    @Transient
    private String businessName;

    @Transient
    private String bookingNumber;

    @Transient
    private String hotelName;

    @Transient
    private String adminResponse;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = DisputeStatus.PENDING;
    }
}
