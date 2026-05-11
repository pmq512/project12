package com.anime.controller;

import com.anime.entity.Anime;
import com.anime.entity.Favorite;
import com.anime.entity.User;
import com.anime.service.AnimeService;
import com.anime.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    private AnimeService animeService;
    
    @PostMapping("/add/{animeId}")
    public String addFavorite(@PathVariable Long animeId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "请先登录");
            return "redirect:/login";
        }
        
        Anime anime = animeService.getAnimeById(animeId);
        if (anime == null) {
            redirectAttributes.addFlashAttribute("error", "动漫不存在");
            return "redirect:/anime/";
        }
        
        if (favoriteService.isFavorite(user, anime)) {
            redirectAttributes.addFlashAttribute("error", "已经收藏过了");
            return "redirect:/anime/view/" + animeId;
        }
        
        favoriteService.addFavorite(user, anime);
        redirectAttributes.addFlashAttribute("success", "收藏成功");
        
        return "redirect:/anime/view/" + animeId;
    }
    
    @PostMapping("/remove/{animeId}")
    public String removeFavorite(@PathVariable Long animeId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "请先登录");
            return "redirect:/login";
        }
        
        Anime anime = animeService.getAnimeById(animeId);
        if (anime == null) {
            redirectAttributes.addFlashAttribute("error", "动漫不存在");
            return "redirect:/anime/";
        }
        
        favoriteService.removeFavorite(user, anime);
        redirectAttributes.addFlashAttribute("success", "取消收藏成功");
        
        return "redirect:/anime/view/" + animeId;
    }
    
    @GetMapping("/list")
    public String listFavorites(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Favorite> favorites = favoriteService.getFavoritesByUser(user);
        model.addAttribute("favorites", favorites);
        model.addAttribute("currentUser", user);
        model.addAttribute("isLoggedIn", true);
        
        return "favorite-list";
    }
}