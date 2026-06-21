package com.example.luxurystay.controller;

import com.example.luxurystay.entity.*;
import com.example.luxurystay.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String userDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "个人中心");
        model.addAttribute("activePage", "dashboard");

        long bookingCount = bookingRepository.countByUserId(user.getId());
        long reviewCount = reviewRepository.countByUserId(user.getId());
        long wishlistCount = wishlistRepository.countByUserId(user.getId());

        Integer totalSpentInt = bookingRepository.sumTotalPriceByUserId(user.getId());
        int totalSpent = totalSpentInt != null ? totalSpentInt : 0;

        List<Booking> upcomingBookings = bookingRepository.findByUserIdAndStatusOrderByCheckInDesc(user.getId(), BookingStatus.CONFIRMED);
        if (upcomingBookings == null) {
            upcomingBookings = List.of();
        } else if (upcomingBookings.size() > 5) {
            upcomingBookings = upcomingBookings.subList(0, 5);
        }
        for (Booking booking : upcomingBookings) {
            hotelRepository.findById(booking.getHotelId()).ifPresent(h -> {
                booking.setHotelName(h.getName());
                booking.setHotelImage(h.getImage());
            });
        }

        List<Review> recentReviews = reviewRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        if (recentReviews == null) {
            recentReviews = List.of();
        } else if (recentReviews.size() > 3) {
            recentReviews = recentReviews.subList(0, 3);
        }
        for (Review review : recentReviews) {
            hotelRepository.findById(review.getHotelId()).ifPresent(h -> review.setHotelName(h.getName()));
        }

        List<Wishlist> wishlists = wishlistRepository.findByUserId(user.getId());
        List<Hotel> wishlistedHotels = wishlists != null ? wishlists.stream()
                .map(w -> hotelRepository.findById(w.getHotelId()).orElse(null))
                .filter(h -> h != null)
                .limit(3)
                .toList() : List.of();

        model.addAttribute("bookingCount", bookingCount);
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("wishlistCount", wishlistCount);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("upcomingBookings", upcomingBookings);
        model.addAttribute("recentReviews", recentReviews);
        model.addAttribute("wishlistedHotels", wishlistedHotels);
        model.addAttribute("content", "user-dashboard");

        return "user-layout";
    }

    @GetMapping("/dashboard")
    public String userDashboardRedirect(HttpSession session, Model model) {
        return userDashboard(session, model);
    }

    @GetMapping("/bookings")
    public String userBookings(@RequestParam(required = false) String status, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "我的预订");
        model.addAttribute("activePage", "bookings");

        long totalBookings = bookingRepository.countByUserId(user.getId());
        long pendingBookings = bookingRepository.countByUserIdAndStatus(user.getId(), BookingStatus.PENDING);
        long completedBookings = bookingRepository.countByUserIdAndStatus(user.getId(), BookingStatus.COMPLETED);
        long disputeBookings = bookingRepository.countByUserIdAndStatus(user.getId(), BookingStatus.DISPUTE);

        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("pendingBookings", pendingBookings);
        model.addAttribute("completedBookings", completedBookings);
        model.addAttribute("disputeBookings", disputeBookings);

        List<Booking> bookings;
        if (status != null && !status.isEmpty()) {
            switch (status) {
                case "upcoming":
                    bookings = bookingRepository.findByUserIdAndStatusOrderByCheckInDesc(user.getId(), BookingStatus.CONFIRMED);
                    break;
                case "completed":
                    bookings = bookingRepository.findByUserIdAndStatusOrderByCheckOutDesc(user.getId(), BookingStatus.COMPLETED);
                    break;
                case "cancelled":
                    bookings = bookingRepository.findByUserIdAndStatusOrderByCreatedAtDesc(user.getId(), BookingStatus.CANCELLED);
                    break;
                default:
                    bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
            }
        } else {
            bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        }

        if (bookings != null) {
            for (Booking booking : bookings) {
                hotelRepository.findById(booking.getHotelId()).ifPresent(h -> {
                    booking.setHotelName(h.getName());
                    booking.setHotelImage(h.getImage());
                    booking.setHotelLocation(h.getLocation());
                });
            }
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("content", "user-bookings");

        return "user-layout";
    }

    @GetMapping("/bookings/detail/{id}")
    public String bookingDetail(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty() || !bookingOpt.get().getUserId().equals(user.getId())) {
            return "redirect:/user/bookings";
        }

        Booking booking = bookingOpt.get();
        hotelRepository.findById(booking.getHotelId()).ifPresent(h -> {
            booking.setHotelName(h.getName());
            booking.setHotelImage(h.getImage());
            booking.setHotelLocation(h.getLocation());
        });

        model.addAttribute("user", user);
        model.addAttribute("booking", booking);
        model.addAttribute("content", "booking-detail");

        return "user-layout";
    }

    @Transactional
    @GetMapping("/bookings/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent() && bookingOpt.get().getUserId().equals(user.getId())) {
            Booking booking = bookingOpt.get();
            if (BookingStatus.CONFIRMED.equals(booking.getStatus()) || 
                BookingStatus.PENDING.equals(booking.getStatus())) {
                booking.setStatus(BookingStatus.USER_CANCELLED);
                booking.setCancelledAt(LocalDateTime.now());
                booking.setCancellationReason("用户主动取消");
                bookingRepository.save(booking);

                hotelRepository.findById(booking.getHotelId()).ifPresent(hotel -> {
                    hotel.setRoomsAvailable(hotel.getRoomsAvailable() + 1);
                    hotelRepository.save(hotel);
                });
            }
        }

        return "redirect:/user/bookings";
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "个人信息");
        model.addAttribute("activePage", "profile");
        model.addAttribute("content", "user-profile");

        return "user-layout";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String idCard,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String birthDate,
            @RequestParam(required = false) String address,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setIdCard(idCard);
        user.setGender(gender);
        if (birthDate != null && !birthDate.isEmpty()) {
            user.setBirthDate(LocalDate.parse(birthDate));
        }
        user.setAddress(address);

        userRepository.save(user);
        session.setAttribute("user", user);

        return "redirect:/user/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "当前密码不正确");
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "个人信息");
            model.addAttribute("activePage", "profile");
            model.addAttribute("content", "user-profile");
            return "user-layout";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "个人信息");
            model.addAttribute("activePage", "profile");
            model.addAttribute("content", "user-profile");
            return "user-layout";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "redirect:/user/profile";
    }

    @GetMapping("/reviews")
    public String userReviews(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "我的评论");
        model.addAttribute("activePage", "reviews");

        List<Review> reviews = reviewRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        if (reviews != null) {
            for (Review review : reviews) {
                hotelRepository.findById(review.getHotelId()).ifPresent(h -> {
                    review.setHotelName(h.getName());
                    review.setHotelImage(h.getImage());
                });
            }
        }
        model.addAttribute("reviews", reviews);
        model.addAttribute("content", "user-reviews");

        return "user-layout";
    }

    @Transactional
    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        Optional<Review> reviewOpt = reviewRepository.findById(id);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            if (review.getUserId().equals(user.getId())) {
                Long hotelId = review.getHotelId();
                reviewRepository.delete(review);
                
                hotelRepository.findById(hotelId).ifPresent(hotel -> {
                    List<Review> approvedReviews = reviewRepository.findByHotelIdAndStatus(hotelId, ReviewStatus.APPROVED);
                    if (!approvedReviews.isEmpty()) {
                        double avgRating = approvedReviews.stream()
                                .mapToInt(Review::getRating)
                                .average()
                                .orElse(0.0);
                        hotel.setRating(Math.round(avgRating * 10) / 10.0);
                        hotel.setReviewCount(approvedReviews.size());
                        hotelRepository.save(hotel);
                    } else {
                        hotel.setRating(0.0);
                        hotel.setReviewCount(0);
                        hotelRepository.save(hotel);
                    }
                });
            }
        }

        return "redirect:/user/reviews";
    }

    @GetMapping("/reviews/edit/{id}")
    public String editReview(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "编辑评论");
        model.addAttribute("activePage", "reviews");
        model.addAttribute("content", "user-reviews");
        return "user-layout";
    }

    @GetMapping("/wishlist")
    public String userWishlist(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "收藏列表");
        model.addAttribute("activePage", "wishlist");

        List<Wishlist> wishlists = wishlistRepository.findByUserId(user.getId());
        List<Hotel> wishlistedHotels = wishlists.stream()
                .map(w -> hotelRepository.findById(w.getHotelId()).orElse(null))
                .filter(h -> h != null)
                .toList();

        model.addAttribute("wishlist", wishlistedHotels);
        model.addAttribute("content", "user-wishlist");

        return "user-layout";
    }

    @Transactional
    @GetMapping("/wishlist/remove/{hotelId}")
    public String removeWishlist(@PathVariable Long hotelId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        wishlistRepository.deleteByUserIdAndHotelId(user.getId(), hotelId);

        return "redirect:/user/wishlist";
    }

    @GetMapping("/wishlist/add/{hotelId}")
    @ResponseBody
    public Map<String, Object> addWishlist(@PathVariable Long hotelId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return response;
        }

        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelId);
        if (hotelOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "酒店不存在");
            return response;
        }

        Optional<Wishlist> existingWishlist = wishlistRepository.findByUserIdAndHotelId(user.getId(), hotelId);
        if (existingWishlist.isPresent()) {
            response.put("success", false);
            response.put("message", "已经收藏过该酒店");
            return response;
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(user.getId());
        wishlist.setHotelId(hotelId);
        wishlistRepository.save(wishlist);

        response.put("success", true);
        response.put("message", "收藏成功");
        return response;
    }

    @GetMapping("/wishlist/check/{hotelId}")
    @ResponseBody
    public Map<String, Object> checkWishlist(@PathVariable Long hotelId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.put("wishlisted", false);
            return response;
        }

        Optional<Wishlist> wishlist = wishlistRepository.findByUserIdAndHotelId(user.getId(), hotelId);
        response.put("wishlisted", wishlist.isPresent());
        return response;
    }
}