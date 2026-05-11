package com.anime.repository;

import com.anime.entity.Anime;
import com.anime.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAnimeIdOrderByCreatedAtDesc(Long animeId);
    List<Comment> findByUserId(Long userId);
}
