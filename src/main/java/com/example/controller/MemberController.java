package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final UserService userService;

    // Constructor injection
    public MemberController(UserService userService) {
        this.userService = userService;
    }

    // ===========================
    //  /member/profile
    // ===========================
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {

        // principal.getName() = email (vì trong UserServiceImpl bạn set username = email)
        String email = principal.getName();

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với email: " + email));

        // Đẩy user sang Thymeleaf
        model.addAttribute("user", user);

        // Trỏ tới templates/member/profile.html
        return "member/profile";
    }

    // (Nếu muốn có dashboard luôn thì thêm luôn)
    @GetMapping("/dashboard")
    public String dashboard() {
        return "member/dashboard"; // templates/member/dashboard.html
    }
}
