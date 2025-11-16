package com.example.controller;

import com.example.model.User;
import com.example.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){ this.authService = authService; }

    @GetMapping("/register")
    public String registerForm(){ return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam(name="username", required=false) String username,
                           @RequestParam(name="name", required=false) String legacyName,
                           @RequestParam(required=false) String email,
                           @RequestParam(required=false) String phone,
                           @RequestParam String password,
                           Model model){
        try{
            String finalName = (username != null && !username.isBlank()) ? username : legacyName;
            if (finalName == null || finalName.isBlank()) {
                model.addAttribute("error","Vui lòng nhập tên đăng nhập.");
                return "register";
            }
            User u = new User();
            u.setName(finalName);
            u.setEmail(email);
            u.setPhone(phone);
            u.setPassword(password);
            u.setRole("USER");   // ⭐ ĐẶT ROLE RÕ RÀNG

            authService.register(u);

            model.addAttribute("msg","Đăng ký thành công. Vui lòng đăng nhập.");
            return "login";
        }catch(Exception ex){
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginForm(){ return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam(required=false) String email,
                        @RequestParam(required=false) String phone,
                        @RequestParam String password,
                        HttpSession session,
                        Model model){
        var userOpt = (email!=null && !email.isEmpty())
                ? authService.loginByEmail(email, password)
                : authService.loginByPhone(phone, password);
        if(userOpt.isPresent()){
            session.setAttribute("user", userOpt.get());
            return "redirect:/";
        } else {
            model.addAttribute("error","Thông tin đăng nhập không chính xác");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/social-login")
    public String socialLogin(@RequestParam String provider, HttpSession session){
        String fakeEmail = provider + "@provider.test";
        String fakeName = "Người dùng " + provider;
        var u = authService.socialLogin(fakeEmail, fakeName);
        session.setAttribute("user", u);
        return "redirect:/";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
        boolean ok = authService.requestPasswordReset(email);
        if (ok) {
            model.addAttribute("message", "Đã gửi liên kết đặt lại mật khẩu tới: " + email);
        } else {
            model.addAttribute("error", "Email không tồn tại trong hệ thống!");
        }
        return "forgot_password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("password") String newPassword,
                                      Model model) {
        boolean changed = authService.resetPassword(token, newPassword);
        if (changed) {
            model.addAttribute("msg","Đặt lại mật khẩu thành công. Vui lòng đăng nhập.");
            return "login";
        } else {
            model.addAttribute("error","Token không hợp lệ hoặc đã hết hạn.");
            model.addAttribute("token", token);
            return "reset_password";
        }
    }
}
