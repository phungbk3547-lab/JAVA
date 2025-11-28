package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;  // ✅ Dùng UserService
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    // ✅ Constructor injection
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // =======================================================
    // 1. ĐĂNG KÝ (REGISTER)
    // =======================================================

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // Nếu redirect có đẩy "user" về thì giữ lại, nếu không thì tạo mới
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               RedirectAttributes redirectAttributes) {
        try {
            // ====== VALIDATION CƠ BẢN ======
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng nhập email.");
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/register";
            }

            // Email đã tồn tại?
            if (userService.findByEmail(user.getEmail()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Email đã tồn tại.");
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/register";
            }

            // Phone đã tồn tại?
            if (user.getPhone() != null && !user.getPhone().isBlank()
                    && userService.findByPhone(user.getPhone()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Số điện thoại đã tồn tại.");
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/register";
            }

            // Gán ROLE mặc định
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }

            // Nếu chưa có name, dùng email làm name
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getEmail());
            }

            // ❗ Mã hóa mật khẩu + lưu DB phải được xử lý bên trong userService.registerUser()
            userService.registerUser(user);

            redirectAttributes.addFlashAttribute("msg", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
        }
    }

    // =======================================================
    // 2. ĐĂNG NHẬP (LOGIN) – để Spring Security xử lý
    // =======================================================

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {

        if (error != null) {
            model.addAttribute("error", "Thông tin đăng nhập không chính xác");
        }
        if (logout != null) {
            model.addAttribute("msg", "Bạn đã đăng xuất thành công.");
        }

        return "login";
    }
    // ❌ KHÔNG cần @PostMapping("/login") nữa
    // Spring Security sẽ tự xử lý POST /login + kiểm tra mật khẩu
    // dựa trên SecurityConfig + UserDetailsService.

    // =======================================================
    // 3. QUÊN MẬT KHẨU (FORGOT PASSWORD)
    // =======================================================

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
        boolean ok = userService.requestPasswordReset(email); // 🔧 Implement trong UserService
        if (ok) {
            model.addAttribute("message", "Đã gửi liên kết đặt lại mật khẩu tới: " + email);
        } else {
            model.addAttribute("error", "Email không tồn tại trong hệ thống!");
        }
        return "forgot_password";
    }

    // =======================================================
    // 4. ĐẶT LẠI MẬT KHẨU (RESET PASSWORD)
    // =======================================================

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token,
                                      @RequestParam("password") String newPassword,
                                      Model model) {
        boolean changed = userService.resetPassword(token, newPassword); // 🔧 Implement trong UserService
        if (changed) {
            model.addAttribute("msg", "Đặt lại mật khẩu thành công. Vui lòng đăng nhập.");
            return "login";
        } else {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            model.addAttribute("token", token);
            return "reset_password";
        }
    }

    // =======================================================
    // (TÙY CHỌN) 5. CÁC HÀM TEST MÃ HÓA MẬT KHẨU / DEBUG
    // =======================================================
    // Nếu bạn còn cần /test/encode, /test/match thì có thể thêm sau
    // nhưng nhớ để logic mã hóa bên Service, không để ở Controller nữa.
}
