package com.example.luxurystay.controller;

import com.example.luxurystay.entity.Booking;
import com.example.luxurystay.entity.BookingStatus;
import com.example.luxurystay.entity.Hotel;
import com.example.luxurystay.entity.User;
import com.example.luxurystay.repository.BookingRepository;
import com.example.luxurystay.repository.HotelRepository;
import com.example.luxurystay.repository.UserRepository;
import com.example.luxurystay.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/confirm")
    public String showBookingConfirm(
            @RequestParam Long hotelId,
            @RequestParam String roomType,
            @RequestParam String checkIn,
            @RequestParam String checkOut,
            @RequestParam int guests,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            // 构建正确的重定向URL，使用URL编码
            String redirectUrl = "/booking/confirm?hotelId=" + hotelId + "&roomType=" + roomType + "&checkIn=" + checkIn + "&checkOut=" + checkOut + "&guests=" + guests;
            return "redirect:/auth/login?redirect=" + java.net.URLEncoder.encode(redirectUrl, java.nio.charset.StandardCharsets.UTF_8);
        }

        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel == null) {
            return "redirect:/hotels";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(checkIn, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOut, formatter);
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        BigDecimal roomRate = BigDecimal.valueOf(hotel.getPrice());
        BigDecimal totalRoomRate = roomRate.multiply(BigDecimal.valueOf(nights));
        BigDecimal tax = totalRoomRate.multiply(new BigDecimal("0.1"));
        BigDecimal deposit = BigDecimal.valueOf(hotel.getDepositAmount() != null ? hotel.getDepositAmount() : 0);
        BigDecimal total = totalRoomRate.add(tax).add(deposit);

        model.addAttribute("hotel", hotel);
        model.addAttribute("roomType", roomType);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("guests", guests);
        model.addAttribute("nights", nights);
        model.addAttribute("roomRate", roomRate);
        model.addAttribute("tax", tax.setScale(2, BigDecimal.ROUND_HALF_UP));
        model.addAttribute("deposit", deposit);
        model.addAttribute("total", total.setScale(2, BigDecimal.ROUND_HALF_UP));

        return "booking-confirm";
    }

    @PostMapping("/confirm")
    public String processBooking(
            @RequestParam Long hotelId,
            @RequestParam String roomType,
            @RequestParam String checkIn,
            @RequestParam String checkOut,
            @RequestParam int guests,
            @RequestParam String guestName,
            @RequestParam String phone,
            @RequestParam(required = false) String email,
            @RequestParam String idCard,
            @RequestParam(required = false) String specialRequests,
            @RequestParam String paymentMethod,
            HttpSession session,
            Model model) {

        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel == null) {
            return "redirect:/hotels";
        }

        User user = (User) session.getAttribute("user");
        Long userId = user != null ? user.getId() : null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkInDate = LocalDate.parse(checkIn, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkOut, formatter);
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        BigDecimal roomRate = BigDecimal.valueOf(hotel.getPrice());
        BigDecimal totalRoomRate = roomRate.multiply(BigDecimal.valueOf(nights));
        BigDecimal tax = totalRoomRate.multiply(new BigDecimal("0.1"));
        BigDecimal deposit = BigDecimal.valueOf(hotel.getDepositAmount() != null ? hotel.getDepositAmount() : 0);
        BigDecimal total = totalRoomRate.add(tax).add(deposit);

        int totalPrice = totalRoomRate.add(tax).intValue();
        int depositAmount = deposit.intValue();

        String bookingNumber = "BK" + System.currentTimeMillis();

        Booking booking = Booking.builder()
                .bookingNumber(bookingNumber)
                .hotelId(hotelId)
                .userId(userId)
                .roomType(roomType)
                .checkIn(checkInDate)
                .checkOut(checkOutDate)
                .adults(guests)
                .children(0)
                .guestName(guestName)
                .phone(phone)
                .email(email)
                .idCard(idCard)
                .specialRequests(specialRequests)
                .paymentMethod(paymentMethod)
                .totalPrice(totalPrice)
                .depositAmount(depositAmount)
                .status(BookingStatus.PENDING)
                .build();

        bookingRepository.save(booking);

        model.addAttribute("booking", booking);
        model.addAttribute("hotel", hotel);
        model.addAttribute("total", total);
        model.addAttribute("deposit", deposit);
        model.addAttribute("roomRate", totalRoomRate);
        model.addAttribute("tax", tax);

        return "payment";
    }

    @GetMapping("/payment/{id}")
    public String showPaymentPage(@PathVariable Long id, Model model) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return "redirect:/hotels";
        }

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        if (hotel == null) {
            return "redirect:/hotels";
        }

        BigDecimal total = BigDecimal.valueOf(booking.getTotalPrice());
        BigDecimal deposit = BigDecimal.valueOf(booking.getDepositAmount());

        model.addAttribute("booking", booking);
        model.addAttribute("hotel", hotel);
        model.addAttribute("total", total);
        model.addAttribute("deposit", deposit);

        return "payment";
    }

    @PostMapping("/payment/{id}")
    public String processPayment(@PathVariable Long id, @RequestParam String paymentMethod, Model model) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return "redirect:/hotels";
        }

        Hotel hotel = hotelRepository.findById(booking.getHotelId()).orElse(null);
        if (hotel == null) {
            return "redirect:/hotels";
        }

        booking.setPaymentMethod(paymentMethod);
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // 减少可用房间
        if (hotel.getRoomsAvailable() > 0) {
            hotel.setRoomsAvailable(hotel.getRoomsAvailable() - 1);
            hotelRepository.save(hotel);
        }

        BigDecimal total = BigDecimal.valueOf(booking.getTotalPrice());

        model.addAttribute("booking", booking);
        model.addAttribute("hotel", hotel);
        model.addAttribute("total", total);

        return "booking-success";
    }
}
