package com.anime.controller;

import com.anime.entity.User;
import com.anime.entity.Anime;
import com.anime.entity.Category;
import com.anime.entity.Comment;
import com.anime.service.UserService;
import com.anime.service.AnimeService;
import com.anime.service.CategoryService;
import com.anime.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AnimeService animeService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String adminDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("isLoggedIn", true);
        model.addAttribute("currentUser", user);
        model.addAttribute("userCount", userService.getAllUsers().size());
        model.addAttribute("animeCount", animeService.getAllAnime().size());
        model.addAttribute("categoryCount", categoryService.getAllCategories().size());
        model.addAttribute("commentCount", commentService.getAllComments().size());

        return "admin-dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("isLoggedIn", true);
        model.addAttribute("currentUser", user);
        model.addAttribute("users", userService.getAllUsers());

        return "admin-users";
    }

    @GetMapping("/anime")
    public String listAnime(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("isLoggedIn", true);
        model.addAttribute("currentUser", user);
        model.addAttribute("animes", animeService.getAllAnime());

        return "admin-anime";
    }

    @GetMapping("/categories")
    public String listCategories(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("isLoggedIn", true);
        model.addAttribute("currentUser", user);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "admin-categories";
    }

    @GetMapping("/comments")
    public String listComments(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("isLoggedIn", true);
        model.addAttribute("currentUser", user);
        model.addAttribute("comments", commentService.getAllComments());

        return "admin-comments";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("success", "用户删除成功");
        return "redirect:/admin/users";
    }

    @GetMapping("/anime/delete/{id}")
    public String deleteAnime(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        animeService.deleteAnime(id);
        redirectAttributes.addFlashAttribute("success", "动漫删除成功");
        return "redirect:/admin/anime";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("success", "分类删除成功");
        return "redirect:/admin/categories";
    }

    @GetMapping("/comments/delete/{id}")
    public String deleteComment(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        commentService.deleteComment(id);
        redirectAttributes.addFlashAttribute("success", "评论删除成功");
        return "redirect:/admin/comments";
    }
}