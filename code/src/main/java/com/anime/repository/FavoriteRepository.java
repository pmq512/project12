package com.anime.repository;

import com.anime.entity.Anime;
import com.anime.entity.Favorite;
import com.anime.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserAndAnime(User user, Anime anime);
    List<Favorite> findByAnime(Anime anime);
    List<Favorite> findByUser(User user);
    boolean existsByUserAndAnime(User user, Anime anime);
}