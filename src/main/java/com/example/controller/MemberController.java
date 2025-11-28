package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final UserService userService; // ✅ GIỮ NGUYÊN

    public MemberController(UserService userService) {
        this.userService = userService;
    }

    // ⭐ Phương thức chung để lấy User Entity đang đăng nhập
    private User getCurrentUser(Principal principal) {
        if (principal == null) return null;
        String email = principal.getName();
        return userService.findByEmail(email).orElse(null);
    }

    // =========================
    // TRANG HỒ SƠ CÁ NHÂN
    // =========================
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy user: " + email));

        model.addAttribute("user", user);

        // Đối tượng dùng để binding cho form cập nhật (trên trang edit)
        model.addAttribute("updatedUser", user);

        return "member/profile";
    }

    // =========================
    // TRANG SỬA HỒ SƠ (NEW)
    // GET /member/profile/edit
    // =========================
    @GetMapping("/profile/edit")
    public String editProfile(Model model, Principal principal) {
        User currentUser = getCurrentUser(principal);
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Nếu chưa có updatedUser trong model (redirect kèm flash attr chẳng hạn)
        if (!model.containsAttribute("updatedUser")) {
            model.addAttribute("updatedUser", currentUser);
        }

        return "member/profile-edit";   // ==> resources/templates/member/profile-edit.html
    }

    // =======================================================
    // Cập nhật hồ sơ cá nhân
    // POST /member/profile/update
    // =======================================================
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("updatedUser") User updatedUser,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(principal);

        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            // Lấy User gốc từ DB
            Optional<User> userOpt = userService.findById(currentUser.getId());
            if (userOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lỗi bảo mật: Không tìm thấy người dùng.");
                return "redirect:/member/profile";
            }
            User userToUpdate = userOpt.get();

            // Cập nhật các trường được phép thay đổi từ form
            userToUpdate.setName(updatedUser.getName());
            userToUpdate.setPhone(updatedUser.getPhone());
            // TODO: nếu sau này có thêm trường avatarUrl, bio... thì set ở đây

            // Lưu vào database
            userService.updateUser(userToUpdate);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật hồ sơ thất bại: " + e.getMessage());
        }

        return "redirect:/member/profile";
    }
}
