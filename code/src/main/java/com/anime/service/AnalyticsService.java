package com.anime.service;

import com.anime.entity.*;
import com.anime.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private ViewHistoryRepository viewHistoryRepository;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private CommentRepository commentRepository;

    public void recordView(User user, Anime anime) {
        Optional<ViewHistory> existing = viewHistoryRepository.findByUserAndAnime(user, anime);
        if (existing.isPresent()) {
            ViewHistory vh = existing.get();
            vh.setViewCount(vh.getViewCount() + 1);
            vh.setViewedAt(java.time.LocalDateTime.now());
            viewHistoryRepository.save(vh);
        } else {
            ViewHistory vh = new ViewHistory();
            vh.setUser(user);
            vh.setAnime(anime);
            viewHistoryRepository.save(vh);
        }
    }

    public Map<String, Object> getDashboardAnalytics() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalUsers", userRepository.count());
        data.put("totalAnimes", animeRepository.count());
        data.put("totalViews", viewHistoryRepository.count());
        data.put("totalRatings", ratingRepository.count());
        data.put("totalFavorites", favoriteRepository.count());
        data.put("totalComments", commentRepository.count());
        data.put("activeUsersToday", viewHistoryRepository.countActiveUsersToday());
        data.put("viewsToday", viewHistoryRepository.countViewsToday());
        return data;
    }

    public List<Map<String, Object>> getPopularAnimes(int limit) {
        List<Object[]> results = viewHistoryRepository.findPopularAnimes();
        return results.stream()
                .limit(limit)
                .map(row -> {
                    Map<String, Object> map = new HashMap<>();
                    Anime anime = (Anime) row[0];
                    map.put("anime", anime);
                    map.put("viewCount", row[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getCategoryDistribution() {
        List<Anime> allAnimes = animeRepository.findAll();
        Map<Category, Long> categoryCount = allAnimes.stream()
                .filter(a -> a.getCategory() != null)
                .collect(Collectors.groupingBy(Anime::getCategory, Collectors.counting()));

        return categoryCount.entrySet().stream()
                .sorted(Map.Entry.<Category, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("category", entry.getKey());
                    map.put("count", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getRatingDistribution() {
        List<Rating> allRatings = ratingRepository.findAll();
        Map<Integer, Long> scoreCount = allRatings.stream()
                .collect(Collectors.groupingBy(Rating::getScore, Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("score", i);
            map.put("count", scoreCount.getOrDefault(i, 0L));
            result.add(map);
        }
        return result;
    }

    public List<Map<String, Object>> getUserActivityStats() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("user", user);
                    map.put("viewCount", viewHistoryRepository.findByUserOrderByViewedAtDesc(user).size());
                    map.put("ratingCount", ratingRepository.findByUser(user).size());
                    map.put("favoriteCount", favoriteRepository.findByUser(user).size());
                    map.put("commentCount", commentRepository.findByUserId(user.getId()).size());
                    return map;
                })
                .sorted((a, b) -> Long.compare(
                        (int) b.get("viewCount") + (int) b.get("ratingCount"),
                        (int) a.get("viewCount") + (int) a.get("ratingCount")))
                .limit(20)
                .collect(Collectors.toList());
    }
}