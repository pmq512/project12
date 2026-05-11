package com.anime.controller;

import com.anime.entity.Anime;
import com.anime.entity.User;
import com.anime.service.AnimeService;
import com.anime.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/rating")
public class RatingController {
    
    @Autowired
    private RatingService ratingService;
    
    @Autowired
    private AnimeService animeService;
    
    @PostMapping("/add/{animeId}")
    public String addRating(@PathVariable Long animeId, 
                           @RequestParam int score,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "请先登录");
            return "redirect:/login";
        }
        
        if (score < 1 || score > 5) {
            redirectAttributes.addFlashAttribute("error", "评分必须在1-5之间");
            return "redirect:/anime/view/" + animeId;
        }
        
        Anime anime = animeService.getAnimeById(animeId);
        if (anime == null) {
            redirectAttributes.addFlashAttribute("error", "动漫不存在");
            return "redirect:/anime/";
        }
        
        ratingService.saveRating(user, anime, score);
        redirectAttributes.addFlashAttribute("success", "评分成功");
        
        return "redirect:/anime/view/" + animeId;
    }
}