package com.example.luxurystay.controller;

import com.example.luxurystay.entity.*;
import com.example.luxurystay.repository.BookingRepository;
import com.example.luxurystay.repository.HotelRepository;
import com.example.luxurystay.repository.ReviewRepository;
import com.example.luxurystay.repository.UserRepository;
import com.example.luxurystay.service.OrderDisputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderDisputeService disputeService;
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @GetMapping
    public String adminDashboard(Model model) {
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long totalBookings = bookingRepository.count();
        long totalHotels = hotelRepository.count();
        long totalUsers = userRepository.count();
        long activeHotels = hotelRepository.countByStatus("ACTIVE");
        long inactiveHotels = hotelRepository.countByStatus("INACTIVE");
        long blacklistedBusinesses = userRepository.countByUserTypeAndBlacklistedTrue(UserType.BUSINESS);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);

        Long totalCommission = bookingRepository.sumTotalCommission();
        Long totalBusinessIncome = bookingRepository.sumTotalBusinessIncome();
        Long totalRevenue = bookingRepository.sumTotalRevenue();

        List<OrderDispute> recentDisputes = disputeService.getDisputesByStatus(DisputeStatus.PENDING);

        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalHotels", totalHotels);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeHotels", activeHotels);
        model.addAttribute("inactiveHotels", inactiveHotels);
        model.addAttribute("blacklistedBusinesses", blacklistedBusinesses);
        model.addAttribute("pendingReviews", pendingReviews);
        model.addAttribute("totalCommission", totalCommission != null ? totalCommission : 0L);
        model.addAttribute("totalBusinessIncome", totalBusinessIncome != null ? totalBusinessIncome : 0L);
        model.addAttribute("totalRevenue", totalRevenue != null ? totalRevenue : 0L);
        model.addAttribute("recentDisputes", recentDisputes);
        model.addAttribute("content", "admin");
        model.addAttribute("pageTitle", "管理员控制台");
        model.addAttribute("pageIcon", "bi bi-shield-lock");

        return "admin-layout";
    }

    @GetMapping("/dashboard")
    public String adminDashboardRedirect(Model model) {
        return adminDashboard(model);
    }

    @GetMapping("/disputes")
    public String viewDisputes(Model model, @RequestParam(required = false) String status) {
        List<OrderDispute> disputes;
        
        if (status != null && !status.isEmpty()) {
            DisputeStatus disputeStatus = DisputeStatus.valueOf(status.toUpperCase());
            disputes = disputeService.getDisputesByStatus(disputeStatus);
        } else {
            disputes = disputeService.getAllDisputes();
        }
        
        // 填充纠纷的关联信息
        for (OrderDispute dispute : disputes) {
            bookingRepository.findById(dispute.getBookingId()).ifPresent(booking -> {
                dispute.setBookingNumber(booking.getBookingNumber());
                userRepository.findById(booking.getUserId()).ifPresent(user -> dispute.setUserName(user.getName()));
                hotelRepository.findById(booking.getHotelId()).ifPresent(hotel -> {
                    dispute.setHotelName(hotel.getName());
                    userRepository.findById(hotel.getBusinessId()).ifPresent(biz -> dispute.setBusinessName(biz.getName()));
                });
            });
            if (dispute.getAdminNote() != null) {
                dispute.setAdminResponse(dispute.getAdminNote());
            }
        }
        
        long totalDisputes = disputeService.countAll();
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long resolvedDisputes = disputeService.countByStatus(DisputeStatus.RESOLVED);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);
        
        model.addAttribute("disputes", disputes);
        model.addAttribute("totalDisputes", totalDisputes);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("content", "admin-disputes");
        model.addAttribute("activePage", "disputes");
        model.addAttribute("pageTitle", "纠纷处理");
        model.addAttribute("pageIcon", "bi bi-exclamation-triangle");
        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("resolvedDisputes", resolvedDisputes);
        model.addAttribute("pendingReviews", pendingReviews);
        return "admin-layout";
    }

    @GetMapping("/disputes/{id}")
    public String viewDisputeDetail(@PathVariable Long id, Model model) {
        OrderDispute dispute = disputeService.getDisputeById(id);
        Booking booking = bookingRepository.findById(dispute.getBookingId()).orElse(null);
        
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);
        
        model.addAttribute("dispute", dispute);
        model.addAttribute("booking", booking);
        model.addAttribute("content", "admin-dispute-detail");
        model.addAttribute("activePage", "disputes");
        model.addAttribute("pageTitle", "纠纷详情");
        model.addAttribute("pageIcon", "bi bi-exclamation-triangle");
        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("pendingReviews", pendingReviews);
        return "admin-layout";
    }

    @GetMapping("/disputes/{id}/process")
    public String processDispute(@PathVariable Long id, Model model) {
        OrderDispute dispute = disputeService.getDisputeById(id);
        disputeService.updateDisputeStatus(id, DisputeStatus.PROCESSING, 1L, "正在处理");
        
        model.addAttribute("success", "已将纠纷标记为处理中");
        return "redirect:/admin/disputes";
    }

    @GetMapping("/disputes/{id}/resolve")
    public String resolveDispute(@PathVariable Long id, @RequestParam String note, Model model) {
        disputeService.updateDisputeStatus(id, DisputeStatus.RESOLVED, 1L, note);
        model.addAttribute("success", "纠纷已解决");
        return "redirect:/admin/disputes";
    }

    @PostMapping("/disputes/resolve")
    public String resolveDisputePost(@RequestParam Long disputeId, @RequestParam String resolution, @RequestParam String adminResponse, Model model) {
        DisputeStatus status;
        if ("USER_WIN".equals(resolution) || "BUSINESS_WIN".equals(resolution) || "COMPROMISE".equals(resolution)) {
            status = DisputeStatus.RESOLVED;
        } else {
            status = DisputeStatus.REJECTED;
        }
        disputeService.updateDisputeStatus(disputeId, status, 1L, adminResponse);
        model.addAttribute("success", "纠纷已处理完成");
        return "redirect:/admin/disputes";
    }

    @GetMapping("/disputes/{id}/reject")
    public String rejectDispute(@PathVariable Long id, @RequestParam String note, Model model) {
        disputeService.updateDisputeStatus(id, DisputeStatus.REJECTED, 1L, note);
        model.addAttribute("success", "已驳回纠纷申请");
        return "redirect:/admin/disputes";
    }

    @GetMapping("/reports")
    public String viewReports(Model model) {
        long totalUsers = userRepository.count();
        long businessUsers = userRepository.countByUserType(UserType.BUSINESS);
        long regularUsers = userRepository.countByUserType(UserType.USER);
        long totalHotels = hotelRepository.count();
        long totalBookings = bookingRepository.count();
        long completedBookings = bookingRepository.countByStatus(BookingStatus.COMPLETED);
        long cancelledBookings = bookingRepository.countByStatus(BookingStatus.CANCELLED);
        long totalReviews = reviewRepository.count();
        long approvedReviews = reviewRepository.countByStatus(ReviewStatus.APPROVED);
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("businessUsers", businessUsers);
        model.addAttribute("regularUsers", regularUsers);
        model.addAttribute("totalHotels", totalHotels);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("completedBookings", completedBookings);
        model.addAttribute("cancelledBookings", cancelledBookings);
        model.addAttribute("totalReviews", totalReviews);
        model.addAttribute("approvedReviews", approvedReviews);
        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("pendingReviews", pendingReviews);
        model.addAttribute("content", "admin-reports");
        model.addAttribute("activePage", "reports");
        model.addAttribute("pageTitle", "数据报表");
        model.addAttribute("pageIcon", "bi bi-bar-chart");
        return "admin-layout";
    }

    @GetMapping("/licenses")
    public String viewLicenses(Model model, @RequestParam(required = false) String status) {
        List<User> businesses = userRepository.findByUserType(UserType.BUSINESS);
        List<User> unverifiedBusinesses = userRepository.findByBusinessLicenseVerifiedFalseAndUserType();
        long unverifiedCount = userRepository.countByBusinessLicenseVerifiedFalseAndUserType();
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);
        
        model.addAttribute("businesses", businesses);
        model.addAttribute("unverifiedBusinesses", unverifiedBusinesses);
        model.addAttribute("unverifiedCount", unverifiedCount);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("content", "admin-licenses");
        model.addAttribute("activePage", "licenses");
        model.addAttribute("pageTitle", "营业执照审核");
        model.addAttribute("pageIcon", "bi bi-file-earmark-check");
        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("pendingReviews", pendingReviews);
        return "admin-layout";
    }

    @GetMapping("/licenses/{id}/verify")
    public String verifyLicense(@PathVariable Long id, @RequestParam boolean verified, @RequestParam(required = false) String note, Model model) {
        try {
            User business = userRepository.findById(id).orElse(null);
            if (business != null) {
                business.setBusinessLicenseVerified(verified);
                userRepository.save(business);
                model.addAttribute("success", verified ? "营业执照已认证通过" : "营业执照认证已驳回");
            }
        } catch (Exception e) {
            model.addAttribute("error", "操作失败: " + e.getMessage());
        }
        return "redirect:/admin/licenses";
    }

    @GetMapping("/users")
    public String viewUsers(Model model, @RequestParam(required = false) String type) {
        List<User> users;
        
        if (type != null && !type.isEmpty()) {
            UserType userType = UserType.valueOf(type.toUpperCase());
            users = userRepository.findByUserType(userType);
        } else {
            users = userRepository.findAll();
        }
        
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);
        
        model.addAttribute("users", users);
        model.addAttribute("selectedType", type);
        model.addAttribute("content", "admin-users");
        model.addAttribute("activePage", "users");
        model.addAttribute("pageTitle", "用户管理");
        model.addAttribute("pageIcon", "bi bi-people");
        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("pendingReviews", pendingReviews);
        return "admin-layout";
    }

    @GetMapping("/users/{id}")
    public String viewUserDetail(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            List<Booking> bookings = bookingRepository.findByUserId(id);
            List<Review> reviews = reviewRepository.findByUserId(id);
            model.addAttribute("user", user);
            model.addAttribute("bookings", bookings);
            model.addAttribute("reviews", reviews);
        }
        
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);
        
        model.addAttribute("content", "admin-user-detail");
        model.addAttribute("activePage", "users");
        model.addAttribute("pageTitle", "用户详情");
        model.addAttribute("pageIcon", "bi bi-person");
        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("pendingReviews", pendingReviews);
        return "admin-layout";
    }

    @GetMapping("/users/{id}/disable")
    public String disableUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setEnabled(false);
            userRepository.save(user);
            model.addAttribute("success", "用户已禁用");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/enable")
    public String enableUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setEnabled(true);
            userRepository.save(user);
            model.addAttribute("success", "用户已启用");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/businesses")
    public String viewBusinesses(Model model, @RequestParam(required = false) String status) {
        List<User> businesses = userRepository.findByUserType(UserType.BUSINESS);
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);
        
        model.addAttribute("businesses", businesses);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("content", "admin-businesses");
        model.addAttribute("activePage", "businesses");
        model.addAttribute("pageTitle", "商家管理");
        model.addAttribute("pageIcon", "bi bi-building");
        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("pendingReviews", pendingReviews);
        return "admin-layout";
    }

    @GetMapping("/businesses/{id}")
    public String viewBusinessDetail(@PathVariable Long id, Model model) {
        User business = userRepository.findById(id).orElse(null);
        if (business != null) {
            List<Hotel> hotels = hotelRepository.findByBusinessId(id);
            model.addAttribute("business", business);
            model.addAttribute("hotels", hotels);
        }
        
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);
        
        model.addAttribute("content", "admin-business-detail");
        model.addAttribute("activePage", "businesses");
        model.addAttribute("pageTitle", "商家详情");
        model.addAttribute("pageIcon", "bi bi-building");
        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("pendingReviews", pendingReviews);
        return "admin-layout";
    }

    @GetMapping("/businesses/{id}/approve")
    public String approveBusiness(@PathVariable Long id, Model model) {
        User business = userRepository.findById(id).orElse(null);
        if (business != null) {
            business.setEnabled(true);
            userRepository.save(business);
            model.addAttribute("success", "商家已通过审核");
        }
        return "redirect:/admin/businesses";
    }

    @GetMapping("/businesses/{id}/reject")
    public String rejectBusiness(@PathVariable Long id, Model model) {
        User business = userRepository.findById(id).orElse(null);
        if (business != null) {
            business.setEnabled(false);
            userRepository.save(business);
            model.addAttribute("success", "商家审核已驳回");
        }
        return "redirect:/admin/businesses";
    }

    @PostMapping("/businesses/{id}/blacklist")
    public String blacklistBusiness(@PathVariable Long id, @RequestParam String reason, Model model) {
        User business = userRepository.findById(id).orElse(null);
        if (business != null) {
            business.setBlacklisted(true);
            business.setBlacklistReason(reason);
            business.setEnabled(false);
            userRepository.save(business);

            List<Hotel> hotels = hotelRepository.findByBusinessId(id);
            for (Hotel hotel : hotels) {
                hotel.setStatus("INACTIVE");
                hotelRepository.save(hotel);
            }
            model.addAttribute("success", "商家已拉黑并下架所有酒店");
        }
        return "redirect:/admin/businesses";
    }

    @PostMapping("/businesses/{id}/unblacklist")
    public String unblacklistBusiness(@PathVariable Long id, Model model) {
        User business = userRepository.findById(id).orElse(null);
        if (business != null) {
            business.setBlacklisted(false);
            business.setBlacklistReason(null);
            userRepository.save(business);
            model.addAttribute("success", "商家已解除拉黑");
        }
        return "redirect:/admin/businesses";
    }

    @GetMapping("/hotels")
    public String viewHotels(Model model, @RequestParam(required = false) String status) {
        List<Hotel> hotels;
        if (status != null && !status.isEmpty()) {
            hotels = hotelRepository.findByStatus(status);
        } else {
            hotels = hotelRepository.findAll();
        }
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);
        
        model.addAttribute("hotels", hotels);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("content", "admin-hotels");
        model.addAttribute("activePage", "hotels");
        model.addAttribute("pageTitle", "酒店管理");
        model.addAttribute("pageIcon", "bi bi-building");
        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("pendingReviews", pendingReviews);
        return "admin-layout";
    }

    @GetMapping("/hotels/{id}/deactivate")
    public String deactivateHotel(@PathVariable Long id, Model model) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if (hotel != null) {
            hotel.setStatus("INACTIVE");
            hotelRepository.save(hotel);
            model.addAttribute("success", "酒店已下架");
        }
        return "redirect:/admin/hotels";
    }

    @GetMapping("/hotels/{id}/activate")
    public String activateHotel(@PathVariable Long id, Model model) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if (hotel != null) {
            hotel.setStatus("ACTIVE");
            hotelRepository.save(hotel);
            model.addAttribute("success", "酒店已上架");
        }
        return "redirect:/admin/hotels";
    }

    @GetMapping("/reviews")
    public String viewReviews(Model model, @RequestParam(required = false) String status) {
        List<Review> reviews;
        
        if (status != null && !status.isEmpty()) {
            ReviewStatus reviewStatus = ReviewStatus.valueOf(status.toUpperCase());
            reviews = reviewRepository.findByStatus(reviewStatus);
        } else {
            reviews = reviewRepository.findAll();
        }
        
        // 设置酒店名称和用户名称
        for (Review review : reviews) {
            hotelRepository.findById(review.getHotelId()).ifPresent(h -> review.setHotelName(h.getName()));
            if (review.getUserName() == null || review.getUserName().isEmpty()) {
                userRepository.findById(review.getUserId()).ifPresent(u -> review.setUserName(u.getName()));
            }
        }
        
        long pendingDisputes = disputeService.countByStatus(DisputeStatus.PENDING);
        long pendingReviews = reviewRepository.countByStatus(ReviewStatus.PENDING);
        
        model.addAttribute("reviews", reviews);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("content", "admin-reviews");
        model.addAttribute("activePage", "reviews");
        model.addAttribute("pageTitle", "评论管理");
        model.addAttribute("pageIcon", "bi bi-chat-dots");
        model.addAttribute("pendingDisputes", pendingDisputes);
        model.addAttribute("pendingReviews", pendingReviews);
        return "admin-layout";
    }

    @GetMapping("/reviews/{id}/approve")
    public String approveReview(@PathVariable Long id, Model model) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            review.setStatus(ReviewStatus.APPROVED);
            reviewRepository.save(review);
            model.addAttribute("success", "评论已通过");
        }
        return "redirect:/admin/reviews";
    }

    @GetMapping("/reviews/{id}/reject")
    public String rejectReview(@PathVariable Long id, Model model) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            review.setStatus(ReviewStatus.REJECTED);
            reviewRepository.save(review);
            model.addAttribute("success", "评论已驳回");
        }
        return "redirect:/admin/reviews";
    }
}