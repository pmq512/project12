package com.example.luxurystay.controller;

import com.example.luxurystay.entity.User;
import com.example.luxurystay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public String viewProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("用户不存在"));
        
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute User user, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User existingUser = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("用户不存在"));
            
            existingUser.setName(user.getName());
            existingUser.setPhone(user.getPhone());
            
            userService.updateUser(existingUser);
            model.addAttribute("success", "个人信息更新成功");
            model.addAttribute("user", existingUser);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
        }
        return "profile";
    }
}