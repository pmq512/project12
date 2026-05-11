package com.anime.service;

import com.anime.entity.Anime;
import com.anime.entity.Comment;
import com.anime.entity.User;
import com.anime.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByAnimeId(Long animeId) {
        return commentRepository.findByAnimeIdOrderByCreatedAtDesc(animeId);
    }

    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment saveComment(String content, Anime anime, User user) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAnime(anime);
        comment.setUser(user);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
