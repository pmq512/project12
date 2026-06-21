package com.example.luxurystay.controller;

import com.example.luxurystay.dto.request.HotelRequest;
import com.example.luxurystay.dto.response.BookingResponse;
import com.example.luxurystay.dto.response.HotelResponse;
import com.example.luxurystay.dto.response.ReviewResponse;
import com.example.luxurystay.entity.User;
import com.example.luxurystay.entity.DisputeStatus;
import com.example.luxurystay.entity.OrderDispute;
import com.example.luxurystay.service.BookingService;
import com.example.luxurystay.service.HotelService;
import com.example.luxurystay.service.OrderDisputeService;
import com.example.luxurystay.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final HotelService hotelService;
    private final ReviewService reviewService;
    private final BookingService bookingService;
    private final OrderDisputeService orderDisputeService;

    @GetMapping
    public String businessDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Long businessId = user.getId();
        List<HotelResponse> hotels = hotelService.getHotelsByBusiness(businessId);
        List<ReviewResponse> pendingReviews = reviewService.getPendingReviewsByBusiness(businessId);
        List<BookingResponse> recentBookings = bookingService.getRecentBookingsByBusiness(businessId);
        
        long totalBookings = bookingService.countBookingsByBusiness(businessId);
        long monthlyBookings = bookingService.countMonthlyBookingsByBusiness(businessId);
        BigDecimal totalRevenue = bookingService.sumRevenueByBusiness(businessId);
        BigDecimal totalCommission = bookingService.sumCommissionByBusiness(businessId);
        BigDecimal businessIncome = bookingService.sumBusinessIncomeByBusiness(businessId);
        
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
        if (totalCommission == null) totalCommission = BigDecimal.ZERO;
        if (businessIncome == null) businessIncome = BigDecimal.ZERO;
        
        model.addAttribute("hotels", hotels);
        model.addAttribute("pendingReviews", pendingReviews.size());
        model.addAttribute("pendingReviewList", pendingReviews);
        model.addAttribute("recentBookings", recentBookings);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("monthlyBookings", monthlyBookings);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalCommission", totalCommission);
        model.addAttribute("businessIncome", businessIncome);
        model.addAttribute("pendingReviewsCount", pendingReviews.size());
        model.addAttribute("pendingBookings", bookingService.countPendingBookingsByBusiness(businessId));
        model.addAttribute("activeMenu", "dashboard");
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("content", "business");
        model.addAttribute("pageTitle", "控制台");
        model.addAttribute("pageIcon", "bi bi-speedometer2");
        
        return "business-layout";
    }

    @GetMapping("/dashboard")
    public String businessDashboardRedirect(HttpSession session, Model model) {
        return businessDashboard(session, model);
    }

    @GetMapping("/hotels")
    public String viewHotels(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Long businessId = user.getId();
        List<HotelResponse> hotels = hotelService.getHotelsByBusiness(businessId);
        model.addAttribute("hotels", hotels);
        model.addAttribute("content", "business-hotels");
        model.addAttribute("activePage", "hotels");
        model.addAttribute("pageTitle", "我的酒店");
        model.addAttribute("pageIcon", "bi bi-building");
        return "business-layout";
    }

    @GetMapping("/hotels/add")
    public String showAddHotelForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("hotelRequest", new HotelRequest());
        model.addAttribute("content", "business-add-hotel");
        model.addAttribute("activePage", "hotels");
        model.addAttribute("pageTitle", "添加酒店");
        model.addAttribute("pageIcon", "bi bi-plus-circle");
        return "business-layout";
    }

    @PostMapping("/hotels")
    public String addHotel(HttpSession session, @ModelAttribute HotelRequest hotelRequest, BindingResult bindingResult, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "请填写完整信息");
            model.addAttribute("content", "business-add-hotel");
            model.addAttribute("activePage", "hotels");
            model.addAttribute("pageTitle", "添加酒店");
            model.addAttribute("pageIcon", "bi bi-plus-circle");
            return "business-layout";
        }

        try {
            Long businessId = user.getId();
            hotelService.createHotel(hotelRequest, businessId);
            model.addAttribute("success", "酒店创建成功");
            return "redirect:/business/hotels";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("content", "business-add-hotel");
            model.addAttribute("activePage", "hotels");
            model.addAttribute("pageTitle", "添加酒店");
            model.addAttribute("pageIcon", "bi bi-plus-circle");
            return "business-layout";
        }
    }

    @GetMapping("/hotels/edit/{id}")
    public String showEditHotelForm(HttpSession session, @PathVariable Long id, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        HotelResponse hotel = hotelService.getHotelById(id);
        model.addAttribute("hotel", hotel);
        model.addAttribute("hotelRequest", HotelRequest.builder()
                .name(hotel.getName())
                .location(hotel.getLocation())
                .description(hotel.getDescription())
                .image(hotel.getImage())
                .star(hotel.getStar())
                .price(hotel.getPrice())
                .facilities(hotel.getFacilities())
                .roomsAvailable(hotel.getRoomsAvailable())
                .build());
        model.addAttribute("content", "business-edit-hotel");
        model.addAttribute("activePage", "hotels");
        model.addAttribute("pageTitle", "编辑酒店");
        model.addAttribute("pageIcon", "bi bi-pencil");
        return "business-layout";
    }

    @PostMapping("/hotels/{id}")
    public String updateHotel(HttpSession session, @PathVariable Long id, @ModelAttribute HotelRequest hotelRequest, 
                             BindingResult bindingResult, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "请填写完整信息");
            model.addAttribute("content", "business-edit-hotel");
            model.addAttribute("activePage", "hotels");
            model.addAttribute("pageTitle", "编辑酒店");
            model.addAttribute("pageIcon", "bi bi-pencil");
            return "business-layout";
        }

        try {
            hotelService.updateHotel(id, hotelRequest);
            model.addAttribute("success", "酒店更新成功");
            return "redirect:/business/hotels";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("content", "business-edit-hotel");
            model.addAttribute("activePage", "hotels");
            model.addAttribute("pageTitle", "编辑酒店");
            model.addAttribute("pageIcon", "bi bi-pencil");
            return "business-layout";
        }
    }

    @GetMapping("/reviews")
    public String viewReviews(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Long businessId = user.getId();
        List<ReviewResponse> pendingReviews = reviewService.getPendingReviewsByBusiness(businessId);
        List<ReviewResponse> approvedReviews = reviewService.getApprovedReviewsByBusiness(businessId);
        List<ReviewResponse> rejectedReviews = reviewService.getRejectedReviewsByBusiness(businessId);
        model.addAttribute("pendingReviews", pendingReviews);
        model.addAttribute("approvedReviews", approvedReviews);
        model.addAttribute("rejectedReviews", rejectedReviews);
        model.addAttribute("pendingReviewsCount", pendingReviews.size());
        model.addAttribute("content", "business-reviews");
        model.addAttribute("activePage", "reviews");
        model.addAttribute("pageTitle", "评论管理");
        model.addAttribute("pageIcon", "bi bi-chat-dots");
        return "business-layout";
    }

    @GetMapping("/reviews/{id}/approve")
    public String approveReview(@PathVariable Long id) {
        reviewService.approveReview(id);
        return "redirect:/business/reviews";
    }

    @GetMapping("/reviews/{id}/reject")
    public String rejectReview(@PathVariable Long id) {
        reviewService.rejectReview(id);
        return "redirect:/business/reviews";
    }

    @PostMapping("/reviews/{id}/reply")
    public String replyToReview(@PathVariable Long id, @RequestParam String replyContent, Model model) {
        try {
            reviewService.replyToReview(id, replyContent);
            model.addAttribute("success", "回复成功");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/business/reviews";
    }

    @GetMapping("/hotels/delete/{id}")
    public String deleteHotel(HttpSession session, @PathVariable Long id, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        try {
            hotelService.deleteHotel(id);
            return "redirect:/business/hotels";
        } catch (Exception e) {
            Long businessId = user.getId();
            List<HotelResponse> hotels = hotelService.getHotelsByBusiness(businessId);
            model.addAttribute("hotels", hotels);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("content", "business-hotels");
            model.addAttribute("activePage", "hotels");
            model.addAttribute("pageTitle", "我的酒店");
            model.addAttribute("pageIcon", "bi bi-building");
            return "business-layout";
        }
    }

    @GetMapping("/bookings")
    public String viewBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Long businessId = user.getId();
        List<BookingResponse> bookings = bookingService.getBookingsByBusiness(businessId);
        model.addAttribute("bookings", bookings);
        model.addAttribute("content", "business-bookings");
        model.addAttribute("activePage", "bookings");
        model.addAttribute("pageTitle", "订单管理");
        model.addAttribute("pageIcon", "bi bi-calendar-check");
        return "business-layout";
    }

    @GetMapping("/bookings/confirm/{id}")
    public String confirmBooking(@PathVariable Long id, Model model) {
        try {
            bookingService.confirmBooking(id);
            model.addAttribute("success", "订单已确认");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/business/bookings";
    }

    @GetMapping("/bookings/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, Model model) {
        try {
            bookingService.businessCancelBooking(id);
            model.addAttribute("success", "订单已取消");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/business/bookings";
    }

    @GetMapping("/bookings/refund/{id}")
    public String refundBooking(@PathVariable Long id, Model model) {
        try {
            bookingService.refundBooking(id);
            model.addAttribute("success", "退款已完成");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/business/bookings";
    }

    @GetMapping("/bookings/complete/{id}")
    public String completeBooking(@PathVariable Long id, Model model) {
        try {
            bookingService.completeBooking(id);
            model.addAttribute("success", "订单已完成");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/business/bookings";
    }

    @GetMapping("/bookings/noshow/{id}")
    public String markNoShow(@PathVariable Long id, Model model) {
        try {
            bookingService.markNoShow(id);
            model.addAttribute("success", "已标记为未入住");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/business/bookings";
    }

    @GetMapping("/bookings/confirm-refund/{id}")
    public String confirmRefund(@PathVariable Long id, Model model) {
        try {
            bookingService.confirmRefund(id);
            model.addAttribute("success", "退款已确认");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/business/bookings";
    }

    @GetMapping("/bookings/reject-refund/{id}")
    public String rejectRefund(@PathVariable Long id, Model model) {
        try {
            bookingService.rejectRefund(id, "商家拒绝退款");
            model.addAttribute("success", "已拒绝退款");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/business/bookings";
    }

    @GetMapping("/bookings/handle-cancel/{id}")
    public String handleUserCancellation(@PathVariable Long id, Model model) {
        try {
            bookingService.handleUserCancellation(id, "用户申请取消");
            model.addAttribute("success", "已处理用户取消请求");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/business/bookings";
    }

    @GetMapping("/disputes")
    public String viewDisputes(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        Long businessId = user.getId();
        List<OrderDispute> disputes = orderDisputeService.getDisputesByBusiness(businessId);
        
        long pendingCount = orderDisputeService.countByBusinessIdAndStatus(businessId, DisputeStatus.PENDING);
        long resolvedCount = orderDisputeService.countByBusinessIdAndStatus(businessId, DisputeStatus.RESOLVED);
        long rejectedCount = orderDisputeService.countByBusinessIdAndStatus(businessId, DisputeStatus.REJECTED);
        
        model.addAttribute("disputes", disputes);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("resolvedCount", resolvedCount);
        model.addAttribute("rejectedCount", rejectedCount);
        model.addAttribute("content", "business-disputes");
        model.addAttribute("activePage", "disputes");
        model.addAttribute("pageTitle", "纠纷管理");
        model.addAttribute("pageIcon", "bi bi-shield-alert");
        return "business-layout";
    }

    @GetMapping("/disputes/{id}")
    public String viewDisputeDetail(HttpSession session, @PathVariable Long id, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        OrderDispute dispute = orderDisputeService.getDisputeById(id);
        model.addAttribute("dispute", dispute);
        model.addAttribute("content", "business-dispute-detail");
        model.addAttribute("activePage", "disputes");
        model.addAttribute("pageTitle", "纠纷详情");
        model.addAttribute("pageIcon", "bi bi-shield-alert");
        return "business-layout";
    }
}