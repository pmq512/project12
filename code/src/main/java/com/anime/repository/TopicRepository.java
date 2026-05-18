package com.anime.repository;

import com.anime.entity.Topic;
import com.anime.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByOrderByPinnedDescCreatedAtDesc();
    List<Topic> findByAuthorOrderByCreatedAtDesc(User author);
    List<Topic> findByTitleContainingIgnoreCase(String keyword);
    long countByAuthor(User author);
}