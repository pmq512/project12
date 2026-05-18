package com.anime.repository;

import com.anime.entity.Anime;
import com.anime.entity.User;
import com.anime.entity.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {
    Optional<ViewHistory> findByUserAndAnime(User user, Anime anime);
    List<ViewHistory> findByUserOrderByViewedAtDesc(User user);
    List<ViewHistory> findByAnimeOrderByViewedAtDesc(Anime anime);

    @Query("SELECT vh.anime, COUNT(vh) as cnt FROM ViewHistory vh GROUP BY vh.anime ORDER BY cnt DESC")
    List<Object[]> findPopularAnimes();

    @Query("SELECT COUNT(DISTINCT vh.user) FROM ViewHistory vh WHERE vh.viewedAt > CURRENT_DATE")
    long countActiveUsersToday();

    @Query("SELECT COUNT(vh) FROM ViewHistory vh WHERE vh.viewedAt > CURRENT_DATE")
    long countViewsToday();

    long count();
}