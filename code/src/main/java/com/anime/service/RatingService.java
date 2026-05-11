package com.anime.service;

import com.anime.entity.Anime;
import com.anime.entity.Rating;
import com.anime.entity.User;
import com.anime.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    public Rating saveRating(User user, Anime anime, int score) {
        Optional<Rating> existingRating = ratingRepository.findByUserAndAnime(user, anime);
        
        Rating rating;
        if (existingRating.isPresent()) {
            rating = existingRating.get();
            rating.setScore(score);
        } else {
            rating = new Rating();
            rating.setUser(user);
            rating.setAnime(anime);
            rating.setScore(score);
        }
        
        return ratingRepository.save(rating);
    }
    
    public Optional<Rating> getRatingByUserAndAnime(User user, Anime anime) {
        return ratingRepository.findByUserAndAnime(user, anime);
    }
    
    public List<Rating> getRatingsByAnime(Anime anime) {
        return ratingRepository.findByAnime(anime);
    }
    
    public List<Rating> getRatingsByUser(User user) {
        return ratingRepository.findByUser(user);
    }
    
    public double getAverageRating(Anime anime) {
        List<Rating> ratings = ratingRepository.findByAnime(anime);
        if (ratings.isEmpty()) {
            return 0.0;
        }
        
        double sum = ratings.stream()
                .mapToInt(Rating::getScore)
                .sum();
        
        return sum / ratings.size();
    }
    
    public int getRatingCount(Anime anime) {
        return (int) ratingRepository.findByAnime(anime).size();
    }
}