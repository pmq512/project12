package com.anime.controller;

import com.anime.entity.Post;
import com.anime.entity.Topic;
import com.anime.entity.User;
import com.anime.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping
    public String listTopics(@RequestParam(value = "keyword", required = false) String keyword,
                             Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Topic> topics;
        if (keyword != null && !keyword.trim().isEmpty()) {
            topics = communityService.searchTopics(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            topics = communityService.getAllTopics();
        }
        model.addAttribute("topics", topics);
        model.addAttribute("isLoggedIn", user != null);
        model.addAttribute("currentUser", user);
        return "community-list";
    }

    @GetMapping("/topic/{id}")
    public String viewTopic(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Topic topic = communityService.getTopicById(id);
        if (topic == null) {
            return "redirect:/community";
        }
        communityService.incrementViewCount(id);
        List<Post> posts = communityService.getPostsByTopic(topic);
        model.addAttribute("topic", topic);
        model.addAttribute("posts", posts);
        model.addAttribute("isLoggedIn", user != null);
        model.addAttribute("currentUser", user);
        return "community-topic";
    }

    @GetMapping("/create")
    public String createTopicForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", user);
        return "community-create";
    }

    @PostMapping("/create")
    public String createTopic(@RequestParam String title,
                              @RequestParam String content,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Topic topic = communityService.createTopic(title, content, user);
        redirectAttributes.addFlashAttribute("success", "话题发布成功");
        return "redirect:/community/topic/" + topic.getId();
    }

    @PostMapping("/topic/{id}/reply")
    public String addReply(@PathVariable Long id,
                           @RequestParam String content,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        communityService.addReply(id, content, user);
        redirectAttributes.addFlashAttribute("success", "回复成功");
        return "redirect:/community/topic/" + id;
    }

    @GetMapping("/topic/{id}/delete")
    public String deleteTopic(@PathVariable Long id, HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        communityService.deleteTopic(id);
        redirectAttributes.addFlashAttribute("success", "话题已删除");
        return "redirect:/community";
    }

    @GetMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        Post post = null;
        communityService.deletePost(id);
        redirectAttributes.addFlashAttribute("success", "回复已删除");
        return "redirect:/community";
    }
}