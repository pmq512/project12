package com.anime.controller;

import com.anime.entity.*;
import com.anime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AnimeService animeService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private DanmakuService danmakuService;

    @GetMapping("/animes")
    public List<Map<String, Object>> getAnimes(@RequestParam(required = false) Long categoryId) {
        List<Anime> animes;
        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            animes = animeService.getAnimeByCategory(category);
        } else {
            animes = animeService.getAllAnime();
        }
        return animes.stream().map(this::animeToMap).collect(Collectors.toList());
    }

    @GetMapping("/animes/{id}")
    public Map<String, Object> getAnime(@PathVariable Long id) {
        Anime anime = animeService.getAnimeById(id);
        if (anime == null) return null;
        Map<String, Object> map = animeToMap(anime);
        map.put("averageRating", String.format("%.1f", ratingService.getAverageRating(anime)));
        map.put("ratingCount", ratingService.getRatingCount(anime));
        map.put("danmakuCount", danmakuService.getDanmakuCount(anime));
        return map;
    }

    @GetMapping("/categories")
    public List<Map<String, Object>> getCategories() {
        return categoryService.getAllCategories().stream().map(cat -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cat.getId());
            map.put("name", cat.getName());
            map.put("description", cat.getDescription());
            map.put("animeCount", animeService.getAnimeByCategory(cat).size());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/categories/{id}")
    public Map<String, Object> getCategory(@PathVariable Long id) {
        Category cat = categoryService.getCategoryById(id);
        if (cat == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("id", cat.getId());
        map.put("name", cat.getName());
        map.put("description", cat.getDescription());
        map.put("animes", animeService.getAnimeByCategory(cat).stream().map(this::animeToMap).collect(Collectors.toList()));
        return map;
    }

    @GetMapping("/users")
    public List<Map<String, Object>> getUsers() {
        return userService.getAllUsers().stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("username", u.getUsername());
            map.put("email", u.getEmail());
            map.put("role", u.getRole());
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public Map<String, Object> getUser(@PathVariable Long id) {
        User u = userService.getUserById(id);
        if (u == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("id", u.getId());
        map.put("username", u.getUsername());
        map.put("email", u.getEmail());
        map.put("role", u.getRole());
        return map;
    }

    @GetMapping("/animes/{id}/comments")
    public List<Map<String, Object>> getComments(@PathVariable Long id) {
        return commentService.getCommentsByAnimeId(id).stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("content", c.getContent());
            map.put("username", c.getUser() != null ? c.getUser().getUsername() : "匿名");
            map.put("createdAt", c.getCreatedAt() != null ? c.getCreatedAt().toString() : null);
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/animes/{id}/ratings")
    public Map<String, Object> getRatings(@PathVariable Long id) {
        Anime anime = animeService.getAnimeById(id);
        if (anime == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("averageRating", String.format("%.1f", ratingService.getAverageRating(anime)));
        map.put("ratingCount", ratingService.getRatingCount(anime));
        map.put("ratings", ratingService.getRatingsByAnime(anime).stream().map(r -> {
            Map<String, Object> rm = new HashMap<>();
            rm.put("id", r.getId());
            rm.put("score", r.getScore());
            rm.put("username", r.getUser() != null ? r.getUser().getUsername() : "匿名");
            return rm;
        }).collect(Collectors.toList()));
        return map;
    }

    @GetMapping("/topics")
    public List<Map<String, Object>> getTopics() {
        return communityService.getAllTopics().stream().map(t -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", t.getId());
            map.put("title", t.getTitle());
            map.put("content", t.getContent());
            map.put("author", t.getAuthor() != null ? t.getAuthor().getUsername() : null);
            map.put("viewCount", t.getViewCount());
            map.put("replyCount", t.getReplyCount());
            map.put("createdAt", t.getCreatedAt() != null ? t.getCreatedAt().toString() : null);
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/topics/{id}")
    public Map<String, Object> getTopic(@PathVariable Long id) {
        Topic t = communityService.getTopicById(id);
        if (t == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("id", t.getId());
        map.put("title", t.getTitle());
        map.put("content", t.getContent());
        map.put("author", t.getAuthor() != null ? t.getAuthor().getUsername() : null);
        map.put("viewCount", t.getViewCount());
        map.put("replyCount", t.getReplyCount());
        map.put("createdAt", t.getCreatedAt() != null ? t.getCreatedAt().toString() : null);
        map.put("posts", communityService.getPostsByTopic(t).stream().map(p -> {
            Map<String, Object> pm = new HashMap<>();
            pm.put("id", p.getId());
            pm.put("content", p.getContent());
            pm.put("author", p.getAuthor() != null ? p.getAuthor().getUsername() : null);
            pm.put("createdAt", p.getCreatedAt() != null ? p.getCreatedAt().toString() : null);
            return pm;
        }).collect(Collectors.toList()));
        return map;
    }

    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam String keyword) {
        return animeService.searchByKeyword(keyword).stream().map(this::animeToMap).collect(Collectors.toList());
    }

    private Map<String, Object> animeToMap(Anime anime) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", anime.getId());
        map.put("title", anime.getTitle());
        map.put("description", anime.getDescription());
        map.put("coverImage", anime.getCoverImage());
        map.put("videoUrl", anime.getVideoUrl());
        if (anime.getCategory() != null) {
            Map<String, Object> catMap = new HashMap<>();
            catMap.put("id", anime.getCategory().getId());
            catMap.put("name", anime.getCategory().getName());
            map.put("category", catMap);
        }
        return map;
    }
}