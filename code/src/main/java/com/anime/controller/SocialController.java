package com.anime.controller;

import com.anime.entity.Notification;
import com.anime.entity.User;
import com.anime.repository.UserRepository;
import com.anime.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/social")
public class SocialController {

    @Autowired
    private SocialService socialService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/follow/{userId}")
    public String followUser(@PathVariable Long userId, RedirectAttributes redirectAttributes, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            socialService.followUser(currentUser.getId(), userId);
            redirectAttributes.addFlashAttribute("success", "关注成功");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/user/profile/" + userId;
    }

    @GetMapping("/unfollow/{userId}")
    public String unfollowUser(@PathVariable Long userId, RedirectAttributes redirectAttributes, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        socialService.unfollowUser(currentUser.getId(), userId);
        redirectAttributes.addFlashAttribute("success", "取消关注成功");

        return "redirect:/user/profile/" + userId;
    }

    @GetMapping("/followers/{userId}")
    public String getFollowers(@PathVariable Long userId, Model model, HttpSession session) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/anime/";
        }

        List<User> followers = socialService.getFollowers(userId);

        User currentUser = (User) session.getAttribute("user");
        boolean isLoggedIn = currentUser != null;

        model.addAttribute("user", user);
        model.addAttribute("followers", followers);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "followers-list";
    }

    @GetMapping("/following/{userId}")
    public String getFollowing(@PathVariable Long userId, Model model, HttpSession session) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/anime/";
        }

        List<User> following = socialService.getFollowing(userId);

        User currentUser = (User) session.getAttribute("user");
        boolean isLoggedIn = currentUser != null;

        model.addAttribute("user", user);
        model.addAttribute("following", following);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "following-list";
    }

    @GetMapping("/notifications")
    public String getNotifications(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Notification> notifications = socialService.getNotifications(currentUser.getId());
        long unreadCount = socialService.getUnreadNotificationCount(currentUser.getId());

        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isLoggedIn", true);

        return "notifications-list";
    }

    @GetMapping("/notifications/read/{id}")
    public String markNotificationAsRead(@PathVariable Long id) {
        socialService.markNotificationAsRead(id);
        return "redirect:/social/notifications";
    }

    @GetMapping("/notifications/read-all")
    public String markAllNotificationsAsRead(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        socialService.markAllNotificationsAsRead(currentUser.getId());

        return "redirect:/social/notifications";
    }

    @GetMapping("/notifications/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        socialService.deleteNotification(id);
        return "redirect:/social/notifications";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam String keyword, Model model, HttpSession session) {
        List<User> users = userRepository.findByUsernameContaining(keyword);

        User currentUser = (User) session.getAttribute("user");
        boolean isLoggedIn = currentUser != null;

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "users-search";
    }
}