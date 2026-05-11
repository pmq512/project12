package com.anime.service;

import com.anime.entity.Anime;
import com.anime.entity.Favorite;
import com.anime.entity.User;
import com.anime.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    public Favorite addFavorite(User user, Anime anime) {
        if (isFavorite(user, anime)) {
            return null; // 已经收藏了
        }
        
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setAnime(anime);
        return favoriteRepository.save(favorite);
    }
    
    public void removeFavorite(User user, Anime anime) {
        favoriteRepository.findByUserAndAnime(user, anime)
                .ifPresent(favoriteRepository::delete);
    }
    
    public boolean isFavorite(User user, Anime anime) {
        return favoriteRepository.existsByUserAndAnime(user, anime);
    }
    
    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findByUser(user);
    }
    
    public List<Favorite> getFavoritesByAnime(Anime anime) {
        return favoriteRepository.findByAnime(anime);
    }
}