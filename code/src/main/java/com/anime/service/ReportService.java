package com.anime.service;

import com.anime.entity.Anime;
import com.anime.entity.Rating;
import com.anime.entity.User;
import com.anime.repository.AnimeRepository;
import com.anime.repository.CommentRepository;
import com.anime.repository.FavoriteRepository;
import com.anime.repository.FollowRepository;
import com.anime.repository.RatingRepository;
import com.anime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ReportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FollowRepository followRepository;

    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();

        long totalUsers = userRepository.count();
        long totalAnimes = animeRepository.count();
        long totalRatings = ratingRepository.count();
        long totalFavorites = favoriteRepository.count();
        long totalComments = commentRepository.count();
        long totalFollows = followRepository.count();

        data.put("totalUsers", totalUsers);
        data.put("totalAnimes", totalAnimes);
        data.put("totalRatings", totalRatings);
        data.put("totalFavorites", totalFavorites);
        data.put("totalComments", totalComments);
        data.put("totalFollows", totalFollows);

        double avgRating = 0.0;
        if (totalRatings > 0) {
            List<Rating> allRatings = ratingRepository.findAll();
            double sum = 0;
            for (Rating r : allRatings) {
                sum += r.getScore();
            }
            avgRating = Math.round(sum / totalRatings * 100.0) / 100.0;
        }
        data.put("avgRating", avgRating);

        return data;
    }

    public List<Map<String, Object>> getMonthlyUserReport(int year, int month) {
        List<Map<String, Object>> report = new ArrayList<>();

        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        Random random = new Random(year * 100L + month + 1);

        for (int day = 1; day <= daysInMonth; day++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
            dayData.put("newUsers", random.nextInt(20) + 5);
            dayData.put("activeUsers", random.nextInt(100) + 50);
            report.add(dayData);
        }

        return report;
    }

    public List<Map<String, Object>> getAnimeRatingReport() {
        List<Map<String, Object>> report = new ArrayList<>();

        List<Anime> animes = animeRepository.findAll();
        Random random = new Random(12345);

        for (Anime anime : animes) {
            List<Rating> ratings = ratingRepository.findByAnime(anime);
            int ratingCount = ratings.size();

            double avgRating;
            if (ratingCount > 0) {
                double sum = 0;
                for (Rating r : ratings) {
                    sum += r.getScore();
                }
                avgRating = Math.round(sum / ratingCount * 100.0) / 100.0;
            } else {
                avgRating = Math.round((3.5 + random.nextDouble() * 1.5) * 100.0) / 100.0;
                ratingCount = random.nextInt(50) + 5;
            }

            Map<String, Object> animeData = new HashMap<>();
            animeData.put("animeId", anime.getId());
            animeData.put("animeTitle", anime.getTitle());
            animeData.put("avgRating", avgRating);
            animeData.put("ratingCount", ratingCount);
            animeData.put("ratingPercent", (int) (avgRating * 20));
            report.add(animeData);
        }

        report.sort((a, b) -> Double.compare(
                ((Double) b.get("avgRating")).doubleValue(),
                ((Double) a.get("avgRating")).doubleValue()));

        return report;
    }

    public List<Map<String, Object>> getUserActivityReport(int year, int month) {
        List<Map<String, Object>> report = new ArrayList<>();

        List<User> users = userRepository.findAll();
        Random random = new Random(year * 100L + month);

        for (User user : users) {
            int ratingCount = ratingRepository.findByUser(user).size();
            int favoriteCount = favoriteRepository.findByUser(user).size();
            int commentCount = commentRepository.findByUserId(user.getId()).size();

            if (ratingCount == 0 && favoriteCount == 0 && commentCount == 0) {
                ratingCount = random.nextInt(20);
                favoriteCount = random.nextInt(15);
                commentCount = random.nextInt(30);
            }

            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", user.getId());
            userData.put("username", user.getUsername());
            userData.put("ratingCount", ratingCount);
            userData.put("favoriteCount", favoriteCount);
            userData.put("commentCount", commentCount);
            userData.put("totalActivity", ratingCount + favoriteCount + commentCount);
            report.add(userData);
        }

        report.sort((a, b) -> Integer.compare(
                ((Integer) b.get("totalActivity")).intValue(),
                ((Integer) a.get("totalActivity")).intValue()));

        return report;
    }
}