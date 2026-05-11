package com.anime.controller;

import com.anime.entity.User;
import com.anime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "用户名已存在");
        }
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "邮箱已被使用");
        }
        if (result.hasErrors()) {
            return "register";
        }
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "注册成功，请登录");
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        // 暂时返回一个空用户，实际应用中需要根据登录状态获取用户信息
        model.addAttribute("user", new User());
        return "profile";
    }
}
