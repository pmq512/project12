package com.anime.service;

import com.anime.entity.Anime;
import com.anime.entity.Category;
import com.anime.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService {
    @Autowired
    private AnimeRepository animeRepository;

    public List<Anime> getAllAnime() {
        return animeRepository.findAll();
    }

    public List<Anime> getAnimeByCategory(Category category) {
        return animeRepository.findByCategory(category);
    }

    public Anime getAnimeById(Long id) {
        return animeRepository.findById(id).orElse(null);
    }

    public Anime saveAnime(Anime anime) {
        return animeRepository.save(anime);
    }

    public void deleteAnime(Long id) {
        animeRepository.deleteById(id);
    }
    
    public List<Anime> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAnime();
        }
        return animeRepository.searchByKeyword(keyword.trim());
    }
    
    public List<Anime> searchByKeywordAndCategory(String keyword, Long categoryId) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return animeRepository.findByCategoryId(categoryId);
        }
        return animeRepository.searchByKeywordAndCategory(keyword.trim(), categoryId);
    }
}
