package com.example.luxurystay.repository;

import com.example.luxurystay.entity.User;
import com.example.luxurystay.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    java.util.List<User> findByUserType(UserType userType);
    long countByUserType(UserType userType);
    
    @org.springframework.data.jpa.repository.Query("SELECT u FROM User u WHERE u.userType = 'BUSINESS' AND u.businessLicenseVerified = false")
    java.util.List<User> findByBusinessLicenseVerifiedFalseAndUserType();
    
    @org.springframework.data.jpa.repository.Query("SELECT u FROM User u WHERE u.userType = 'BUSINESS' AND u.businessLicenseVerified = true")
    java.util.List<User> findByBusinessLicenseVerifiedTrueAndUserType();
    
    @org.springframework.data.jpa.repository.Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'BUSINESS' AND u.businessLicenseVerified = false")
    long countByBusinessLicenseVerifiedFalseAndUserType();

    long countByUserTypeAndBlacklistedTrue(UserType userType);
}
