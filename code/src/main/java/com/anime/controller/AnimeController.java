package com.anime.controller;

import com.anime.entity.Anime;
import com.anime.entity.Category;
import com.anime.entity.Comment;
import com.anime.entity.User;
import com.anime.service.AnimeService;
import com.anime.service.CategoryService;
import com.anime.service.CommentService;
import com.anime.service.UserService;
import com.anime.service.VideoService;
import com.anime.service.RatingService;
import com.anime.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/anime")
public class AnimeController {
    @Autowired
    private AnimeService animeService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private VideoService videoService;
    
    @Autowired
    private RatingService ratingService;
    
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/")
    public String listAnime(@RequestParam(value = "categoryId", required = false) Long categoryId,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("isLoggedIn", user != null);
        model.addAttribute("currentUser", user);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        
        model.addAttribute("categories", categoryService.getAllCategories());
        
        if (categoryId != null) {
            if (keyword != null && !keyword.trim().isEmpty()) {
                model.addAttribute("animes", animeService.searchByKeywordAndCategory(keyword, categoryId));
            } else {
                Category category = categoryService.getCategoryById(categoryId);
                if (category != null) {
                    model.addAttribute("animes", animeService.getAnimeByCategory(category));
                } else {
                    model.addAttribute("animes", animeService.getAllAnime());
                }
            }
        } else {
            if (keyword != null && !keyword.trim().isEmpty()) {
                model.addAttribute("animes", animeService.searchByKeyword(keyword));
            } else {
                model.addAttribute("animes", animeService.getAllAnime());
            }
        }
        return "anime-list";
    }

    @GetMapping("/view/{id}")
    public String viewAnime(@PathVariable Long id, Model model, HttpSession session) {
        Anime anime = animeService.getAnimeById(id);
        String processedUrl = videoService.processVideoUrl(anime.getVideoUrl());
        boolean isEmbed = videoService.isEmbedUrl(processedUrl);
        
        // 获取评分信息
        double averageRating = ratingService.getAverageRating(anime);
        int ratingCount = ratingService.getRatingCount(anime);
        
        // 获取收藏状态
        User user = (User) session.getAttribute("user");
        boolean isFavorite = false;
        if (user != null) {
            isFavorite = favoriteService.isFavorite(user, anime);
        }
        
        model.addAttribute("anime", anime);
        model.addAttribute("videoUrl", processedUrl);
        model.addAttribute("isEmbedUrl", isEmbed);
        model.addAttribute("averageRating", String.format("%.1f", averageRating));
        model.addAttribute("ratingCount", ratingCount);
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("comments", commentService.getCommentsByAnimeId(id));
        model.addAttribute("isLoggedIn", user != null);
        model.addAttribute("currentUser", user);
        return "anime-view";
    }
    
    @PostMapping("/comment/{animeId}")
    public String addComment(@PathVariable Long animeId, @RequestParam String content, 
                            RedirectAttributes redirectAttributes) {
        // 暂时使用一个默认用户，实际应用中需要根据登录状态获取用户信息
        User user = new User();
        user.setUsername("guest");
        
        Anime anime = animeService.getAnimeById(animeId);
        
        if (content != null && !content.trim().isEmpty()) {
            commentService.saveComment(content, anime, user);
            redirectAttributes.addFlashAttribute("success", "评论发布成功");
        }
        
        return "redirect:/anime/view/" + animeId;
    }

    @GetMapping("/add")
    public String addAnimeForm(Model model) {
        model.addAttribute("anime", new Anime());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "anime-form";
    }

    @PostMapping("/save")
    public String saveAnime(@ModelAttribute Anime anime) {
        animeService.saveAnime(anime);
        return "redirect:/anime/";
    }

    @GetMapping("/edit/{id}")
    public String editAnimeForm(@PathVariable Long id, Model model) {
        model.addAttribute("anime", animeService.getAnimeById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "anime-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnime(@PathVariable Long id) {
        animeService.deleteAnime(id);
        return "redirect:/anime/";
    }
}
