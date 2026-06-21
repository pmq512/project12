package com.example.luxurystay.controller;

import com.example.luxurystay.dto.request.LoginRequest;
import com.example.luxurystay.dto.request.RegisterRequest;
import com.example.luxurystay.dto.response.ApiResponse;
import com.example.luxurystay.dto.response.JwtResponse;
import com.example.luxurystay.entity.User;
import com.example.luxurystay.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(required = false) String redirect, HttpSession session) {
        model.addAttribute("loginRequest", new LoginRequest());
        if (redirect != null && !redirect.isEmpty()) {
            try {
                // 解码redirect参数
                String decodedRedirect = java.net.URLDecoder.decode(redirect, "UTF-8");
                session.setAttribute("redirectUrl", decodedRedirect);
            } catch (Exception e) {
                session.setAttribute("redirectUrl", redirect);
            }
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest, 
                       BindingResult bindingResult, Model model,
                       HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "请填写正确的邮箱和密码");
            return "login";
        }

        try {
            JwtResponse response = userService.login(loginRequest);
            
            // 手动设置 SecurityContext 并保存到 session
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
            
            // 设置 session 中的 user 对象
            User user = userService.findByEmail(loginRequest.getEmail()).orElse(null);
            if (user != null) {
                session.setAttribute("user", user);
                
                String redirectUrl = (String) session.getAttribute("redirectUrl");
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    session.removeAttribute("redirectUrl");
                    return "redirect:" + redirectUrl;
                }
                
                switch (user.getUserType()) {
                    case ADMIN:
                        return "redirect:/admin";
                    case BUSINESS:
                        return "redirect:/business";
                    case USER:
                    default:
                        return "redirect:/";
                }
            }
            
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "邮箱或密码错误");
            return "login";
        }
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "请填写完整信息");
            return "register";
        }

        try {
            User user = userService.register(registerRequest);
            model.addAttribute("success", "注册成功，请登录");
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @PostMapping("/api/login")
    public ResponseEntity<ApiResponse<JwtResponse>> apiLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse response = userService.login(loginRequest);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, e.getMessage()));
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<ApiResponse<User>> apiRegister(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("注册成功", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}