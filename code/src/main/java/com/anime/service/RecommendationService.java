package com.anime.service;

import com.anime.entity.Anime;
import com.anime.entity.Favorite;
import com.anime.entity.Rating;
import com.anime.entity.User;
import com.anime.repository.AnimeRepository;
import com.anime.repository.FavoriteRepository;
import com.anime.repository.RatingRepository;
import com.anime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Anime> getRecommendations(Long userId, int limit) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return getPopularAnimes(limit);
        }

        Set<Long> userAnimeIds = new HashSet<>();
        List<Favorite> userFavorites = favoriteRepository.findByUser(user);
        List<Rating> userRatings = ratingRepository.findByUser(user);

        userFavorites.forEach(f -> userAnimeIds.add(f.getAnime().getId()));
        userRatings.forEach(r -> userAnimeIds.add(r.getAnime().getId()));

        List<Anime> recommendations = new ArrayList<>();

        List<Anime> similarUserRecommendations = getRecommendationsFromSimilarUsers(userId, userAnimeIds, limit);
        recommendations.addAll(similarUserRecommendations);

        List<Anime> categoryRecommendations = getRecommendationsFromCategories(userId, userAnimeIds, limit);
        recommendations.addAll(categoryRecommendations);

        List<Anime> popularRecommendations = getPopularAnimes(limit);
        recommendations.addAll(popularRecommendations);

        recommendations = recommendations.stream()
                .filter(a -> !userAnimeIds.contains(a.getId()))
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());

        if (recommendations.isEmpty()) {
            recommendations = getPopularAnimes(limit);
        }

        return recommendations;
    }

    private List<Anime> getRecommendationsFromSimilarUsers(Long userId, Set<Long> userAnimeIds, int limit) {
        List<User> allUsers = userRepository.findAll();
        Map<User, Double> userSimilarity = new HashMap<>();

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }

        List<Rating> targetUserRatings = ratingRepository.findByUser(user);
        Map<Long, Double> targetUserRatingMap = targetUserRatings.stream()
                .collect(Collectors.toMap(r -> r.getAnime().getId(), r -> r.getScore().doubleValue()));

        for (User otherUser : allUsers) {
            if (otherUser.getId().equals(userId)) continue;

            List<Rating> otherUserRatings = ratingRepository.findByUser(otherUser);
            Map<Long, Double> otherUserRatingMap = otherUserRatings.stream()
                    .collect(Collectors.toMap(r -> r.getAnime().getId(), r -> r.getScore().doubleValue()));

            double similarity = calculateCosineSimilarity(targetUserRatingMap, otherUserRatingMap);
            if (similarity > 0) {
                userSimilarity.put(otherUser, similarity);
            }
        }

        List<User> similarUsers = userSimilarity.entrySet().stream()
                .sorted(Map.Entry.<User, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Map<Anime, Double> recommendationScores = new HashMap<>();
        for (User similarUser : similarUsers) {
            List<Favorite> similarUserFavorites = favoriteRepository.findByUser(similarUser);
            List<Rating> similarUserRatings = ratingRepository.findByUser(similarUser);

            double weight = userSimilarity.get(similarUser);

            for (Favorite fav : similarUserFavorites) {
                if (!userAnimeIds.contains(fav.getAnime().getId())) {
                    recommendationScores.merge(fav.getAnime(), weight * 2, Double::sum);
                }
            }

            for (Rating rating : similarUserRatings) {
                if (!userAnimeIds.contains(rating.getAnime().getId())) {
                    double score = weight * rating.getScore().doubleValue();
                    recommendationScores.merge(rating.getAnime(), score, Double::sum);
                }
            }
        }

        return recommendationScores.entrySet().stream()
                .sorted(Map.Entry.<Anime, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calculateCosineSimilarity(Map<Long, Double> ratings1, Map<Long, Double> ratings2) {
        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (Long animeId : ratings1.keySet()) {
            if (ratings2.containsKey(animeId)) {
                dotProduct += ratings1.get(animeId) * ratings2.get(animeId);
            }
            norm1 += Math.pow(ratings1.get(animeId), 2);
        }

        for (Double rating : ratings2.values()) {
            norm2 += Math.pow(rating, 2);
        }

        if (norm1 == 0 || norm2 == 0) return 0;
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private List<Anime> getRecommendationsFromCategories(Long userId, Set<Long> userAnimeIds, int limit) {
        Map<Long, Integer> categoryCounts = new HashMap<>();

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }

        List<Favorite> userFavorites = favoriteRepository.findByUser(user);
        List<Rating> userRatings = ratingRepository.findByUser(user);

        for (Favorite fav : userFavorites) {
            Long categoryId = fav.getAnime().getCategory().getId();
            categoryCounts.merge(categoryId, 2, Integer::sum);
        }

        for (Rating rating : userRatings) {
            if (rating.getScore() >= 4) {
                Long categoryId = rating.getAnime().getCategory().getId();
                categoryCounts.merge(categoryId, rating.getScore(), Integer::sum);
            }
        }

        if (categoryCounts.isEmpty()) {
            return Collections.emptyList();
        }

        Long topCategoryId = categoryCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (topCategoryId == null) {
            return Collections.emptyList();
        }

        return animeRepository.findByCategoryId(topCategoryId).stream()
                .filter(a -> !userAnimeIds.contains(a.getId()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Anime> getPopularAnimes(int limit) {
        List<Rating> allRatings = ratingRepository.findAll();
        Map<Long, Double> animeRatingScores = new HashMap<>();
        Map<Long, Integer> animeRatingCounts = new HashMap<>();

        for (Rating rating : allRatings) {
            Long animeId = rating.getAnime().getId();
            animeRatingScores.merge(animeId, rating.getScore().doubleValue(), Double::sum);
            animeRatingCounts.merge(animeId, 1, Integer::sum);
        }

        Map<Anime, Double> popularityScores = new HashMap<>();
        for (Map.Entry<Long, Double> entry : animeRatingScores.entrySet()) {
            Long animeId = entry.getKey();
            double avgScore = entry.getValue() / animeRatingCounts.get(animeId);
            int count = animeRatingCounts.get(animeId);
            double popularityScore = avgScore * Math.log(count + 1);

            animeRepository.findById(animeId).ifPresent(anime -> {
                popularityScores.put(anime, popularityScore);
            });
        }

        return popularityScores.entrySet().stream()
                .sorted(Map.Entry.<Anime, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Anime> getNewestAnimes(int limit) {
        return animeRepository.findAllByOrderByIdDesc().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Anime> getRecommendationsForGuest(int limit) {
        return getPopularAnimes(limit);
    }
}