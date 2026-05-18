package com.anime.controller;

import com.anime.entity.Anime;
import com.anime.entity.User;
import com.anime.repository.UserRepository;
import com.anime.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/recommend")
    public String getRecommendations(Model model, HttpSession session) {
        List<Anime> recommendations;
        boolean isLoggedIn = false;
        
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            recommendations = recommendationService.getRecommendations(currentUser.getId(), 12);
            model.addAttribute("currentUser", currentUser);
            isLoggedIn = true;
        } else {
            recommendations = recommendationService.getRecommendationsForGuest(12);
        }
        
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("isLoggedIn", isLoggedIn);
        
        return "recommendation-list";
    }
}