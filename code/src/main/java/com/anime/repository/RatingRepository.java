package com.anime.repository;

import com.anime.entity.Anime;
import com.anime.entity.Rating;
import com.anime.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserAndAnime(User user, Anime anime);
    List<Rating> findByAnime(Anime anime);
    List<Rating> findByUser(User user);
}