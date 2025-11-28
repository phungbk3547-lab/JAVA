package com.example.controller;

import com.example.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    // Khuyến khích dùng constructor injection thay vì @Autowired field
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 1. DASHBOARD: Thống kê & Duyệt bài
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Cards thống kê
        model.addAttribute("totalUsers", adminService.countTotalUsers());
        model.addAttribute("activeCount", adminService.countActivePosts());
        model.addAttribute("pendingCount", adminService.countPendingPosts());
        model.addAttribute("complaintCount", adminService.countPendingComplaints());
        model.addAttribute("recentUsers", adminService.getRecentUsers());

        // Danh sách bài (tuỳ logic: có thể là tất cả hoặc chỉ pending)
        model.addAttribute("allPosts", adminService.getAllPosts());

        // View: src/main/resources/templates/admin/dashboard.html
        return "admin/dashboard";
    }

    // Action: Duyệt / Từ chối bài từ Dashboard
    @PostMapping("/post/approve/{id}")
    public String approvePost(@PathVariable Integer id) {
        adminService.approvePost(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/post/reject/{id}")
    public String rejectPost(@PathVariable Integer id) {
        adminService.rejectPost(id);
        return "redirect:/admin/dashboard";
    }

    // --- TRANG KHIẾU NẠI ---
    @GetMapping("/complaint")
    public String showComplaints(Model model) {
        model.addAttribute("complaints", adminService.getAllComplaints());
        model.addAttribute("complaintCount", adminService.countPendingComplaints());
        // View: src/main/resources/templates/admin/complaint.html
        return "admin/complaint";
    }

    // Action 1: Bỏ qua báo cáo (Giữ bài)
    @PostMapping("/complaint/dismiss/{id}")
    public String dismissComplaint(@PathVariable Integer id) {
        adminService.dismissComplaint(id);
        return "redirect:/admin/complaint";
    }

    // Action 2: Xác nhận vi phạm (Đánh dấu bài lỗi)
    @PostMapping("/complaint/accept/{id}")
    public String acceptComplaint(@PathVariable Integer id) {
        adminService.acceptComplaint(id);
        return "redirect:/admin/complaint";
    }
}
