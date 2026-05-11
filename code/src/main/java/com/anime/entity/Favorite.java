package com.anime.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "anime_id")
    private Anime anime;
    
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Anime getAnime() {
        return anime;
    }
    
    public void setAnime(Anime anime) {
        this.anime = anime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}