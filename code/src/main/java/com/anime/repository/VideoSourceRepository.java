package com.anime.repository;

import com.anime.entity.Anime;
import com.anime.entity.VideoSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoSourceRepository extends JpaRepository<VideoSource, Long> {
    List<VideoSource> findByAnimeOrderByIdAsc(Anime anime);
    List<VideoSource> findByAnimeIdOrderByIdAsc(Long animeId);
    void deleteByAnime(Anime anime);
}