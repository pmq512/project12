package com.example.luxurystay.service;

import com.example.luxurystay.entity.*;
import com.example.luxurystay.repository.BookingRepository;
import com.example.luxurystay.repository.OrderDisputeRepository;
import com.example.luxurystay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDisputeService {

    private final OrderDisputeRepository disputeRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderDispute createDispute(Long bookingId, Long applicantId, UserType applicantType, String reason, String evidence) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));

        if (!booking.getUserId().equals(applicantId)) {
            throw new IllegalArgumentException("无权申请介入此订单");
        }

        OrderDispute dispute = OrderDispute.builder()
                .bookingId(bookingId)
                .applicantId(applicantId)
                .applicantType(applicantType)
                .reason(reason)
                .evidence(evidence)
                .status(DisputeStatus.PENDING)
                .build();

        return disputeRepository.save(dispute);
    }

    public List<OrderDispute> getAllDisputes() {
        return disputeRepository.findAll();
    }

    public List<OrderDispute> getDisputesByStatus(DisputeStatus status) {
        return disputeRepository.findByStatus(status);
    }

    public List<OrderDispute> getDisputesByUser(Long userId, UserType userType) {
        return disputeRepository.findByApplicantIdAndApplicantType(userId, userType);
    }

    public OrderDispute getDisputeById(Long id) {
        return disputeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("纠纷不存在"));
    }

    @Transactional
    public OrderDispute updateDisputeStatus(Long id, DisputeStatus status, Long adminId, String adminNote) {
        OrderDispute dispute = getDisputeById(id);
        dispute.setStatus(status);
        dispute.setAdminId(adminId);
        dispute.setAdminNote(adminNote);
        
        if (status == DisputeStatus.RESOLVED || status == DisputeStatus.REJECTED) {
            dispute.setResolvedAt(LocalDateTime.now());
        }
        
        return disputeRepository.save(dispute);
    }

    public long countByStatus(DisputeStatus status) {
        return disputeRepository.countByStatus(status);
    }

    public long countAll() {
        return disputeRepository.count();
    }

    public List<OrderDispute> getDisputesByBusiness(Long businessId) {
        return disputeRepository.findByBusinessId(businessId);
    }

    public long countByBusinessIdAndStatus(Long businessId, DisputeStatus status) {
        return disputeRepository.countByBusinessIdAndStatus(businessId, status);
    }
}