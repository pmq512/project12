package com.anime.repository;

import com.anime.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByUsernameContaining(String username);
    
    @Query("SELECT u.id, u.username, " +
           "(SELECT COUNT(r) FROM Rating r WHERE r.user = u AND r.createdAt BETWEEN :start AND :end) as ratingCount, " +
           "(SELECT COUNT(f) FROM Favorite f WHERE f.user = u AND f.createdAt BETWEEN :start AND :end) as favoriteCount, " +
           "(SELECT COUNT(c) FROM Comment c WHERE c.user = u AND c.createdAt BETWEEN :start AND :end) as commentCount " +
           "FROM User u ORDER BY ratingCount + favoriteCount + commentCount DESC")
    List<Object[]> getUserActivityStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}