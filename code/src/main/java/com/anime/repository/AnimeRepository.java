package com.anime.repository;

import com.anime.entity.Anime;
import com.anime.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
    List<Anime> findByCategory(Category category);
    
    List<Anime> findByTitleContaining(String title);
    
    @Query("SELECT a FROM Anime a WHERE a.title LIKE %:keyword% OR a.description LIKE %:keyword%")
    List<Anime> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT a FROM Anime a WHERE a.category.id = :categoryId")
    List<Anime> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT a FROM Anime a WHERE (a.title LIKE %:keyword% OR a.description LIKE %:keyword%) AND a.category.id = :categoryId")
    List<Anime> searchByKeywordAndCategory(@Param("keyword") String keyword, @Param("categoryId") Long categoryId);
    
    List<Anime> findAllByOrderByIdDesc();
}