package com.anime.controller;

import com.anime.entity.User;
import com.anime.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        Map<String, Object> dashboardData = reportService.getDashboardData();
        model.addAttribute("data", dashboardData);
        model.addAttribute("currentUser", user);
        model.addAttribute("isLoggedIn", true);

        return "report-dashboard";
    }

    @GetMapping("/users")
    public String userReport(@RequestParam(value = "year", required = false) Integer year,
                             @RequestParam(value = "month", required = false) Integer month,
                             Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        if (year == null || month == null) {
            LocalDate now = LocalDate.now();
            year = now.getYear();
            month = now.getMonthValue();
        }

        List<Map<String, Object>> userReport = reportService.getMonthlyUserReport(year, month);
        model.addAttribute("report", userReport);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("currentUser", user);
        model.addAttribute("isLoggedIn", true);

        return "report-users";
    }

    @GetMapping("/anime-ratings")
    public String animeRatingReport(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        List<Map<String, Object>> ratingReport = reportService.getAnimeRatingReport();
        model.addAttribute("report", ratingReport);
        model.addAttribute("currentUser", user);
        model.addAttribute("isLoggedIn", true);

        return "report-anime-ratings";
    }

    @GetMapping("/user-activity")
    public String userActivityReport(@RequestParam(value = "year", required = false) Integer year,
                                     @RequestParam(value = "month", required = false) Integer month,
                                     Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        if (year == null || month == null) {
            LocalDate now = LocalDate.now();
            year = now.getYear();
            month = now.getMonthValue();
        }

        List<Map<String, Object>> activityReport = reportService.getUserActivityReport(year, month);
        model.addAttribute("report", activityReport);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("currentUser", user);
        model.addAttribute("isLoggedIn", true);

        return "report-user-activity";
    }
}