package com.anime.service;

import com.anime.entity.Post;
import com.anime.entity.Topic;
import com.anime.entity.User;
import com.anime.repository.PostRepository;
import com.anime.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommunityService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    public List<Topic> getAllTopics() {
        return topicRepository.findByOrderByPinnedDescCreatedAtDesc();
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }

    @Transactional
    public Topic createTopic(String title, String content, User author) {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setAuthor(author);
        return topicRepository.save(topic);
    }

    @Transactional
    public Post addReply(Long topicId, String content, User author) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        if (topic == null) return null;

        Post post = new Post();
        post.setTopic(topic);
        post.setContent(content);
        post.setAuthor(author);
        post = postRepository.save(post);

        topic.setReplyCount((int) postRepository.countByTopic(topic));
        topic.setUpdatedAt(LocalDateTime.now());
        topicRepository.save(topic);

        return post;
    }

    @Transactional
    public void incrementViewCount(Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElse(null);
        if (topic != null) {
            topic.setViewCount(topic.getViewCount() + 1);
            topicRepository.save(topic);
        }
    }

    public List<Post> getPostsByTopic(Topic topic) {
        return postRepository.findByTopicOrderByCreatedAtAsc(topic);
    }

    public List<Topic> searchTopics(String keyword) {
        return topicRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            Topic topic = post.getTopic();
            postRepository.deleteById(id);
            topic.setReplyCount((int) postRepository.countByTopic(topic));
            topicRepository.save(topic);
        }
    }
}