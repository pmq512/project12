package com.anime.controller;

import com.anime.entity.User;
import com.anime.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("dashboard", analyticsService.getDashboardAnalytics());
        model.addAttribute("popularAnimes", analyticsService.getPopularAnimes(10));
        model.addAttribute("categoryDistribution", analyticsService.getCategoryDistribution());
        model.addAttribute("ratingDistribution", analyticsService.getRatingDistribution());
        model.addAttribute("userActivityStats", analyticsService.getUserActivityStats());
        model.addAttribute("currentUser", user);
        return "admin-analytics";
    }
}