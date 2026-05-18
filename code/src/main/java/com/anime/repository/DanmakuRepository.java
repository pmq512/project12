package com.anime.repository;

import com.anime.entity.Anime;
import com.anime.entity.Danmaku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanmakuRepository extends JpaRepository<Danmaku, Long> {
    List<Danmaku> findByAnimeOrderByTimeAsc(Anime anime);
    List<Danmaku> findByAnimeIdOrderByTimeAsc(Long animeId);
    long countByAnime(Anime anime);
}