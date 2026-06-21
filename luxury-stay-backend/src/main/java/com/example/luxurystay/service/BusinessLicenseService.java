package com.example.luxurystay.service;

import com.example.luxurystay.entity.User;
import com.example.luxurystay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessLicenseService {

    private final UserRepository userRepository;

    @Transactional
    public User verifyBusinessLicense(Long businessId, boolean verified) {
        User business = userRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException("商家不存在"));
        
        business.setBusinessLicenseVerified(verified);
        return userRepository.save(business);
    }

    public List<User> getBusinessesWithUnverifiedLicense() {
        return userRepository.findByBusinessLicenseVerifiedFalseAndUserType();
    }

    public List<User> getBusinessesWithVerifiedLicense() {
        return userRepository.findByBusinessLicenseVerifiedTrueAndUserType();
    }

    public boolean isLicenseVerified(Long businessId) {
        return userRepository.findById(businessId)
                .map(User::getBusinessLicenseVerified)
                .orElse(false);
    }

    public long countUnverifiedLicenses() {
        return userRepository.countByBusinessLicenseVerifiedFalseAndUserType();
    }
}