package com.example.service;

import com.example.model.Complaint;
import com.example.model.Post;
import com.example.model.User;
import com.example.repository.ComplaintRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private PostRepository postRepo;
    @Autowired
    private ComplaintRepository complaintRepo;
    @Autowired
    private UserRepository userRepo;

    // ==========================================
    // 1. THỐNG KÊ DASHBOARD (STATS CARDS)
    // ==========================================

    public long countTotalUsers() {
        return userRepo.count();
    }

    public long countActivePosts() {
        return postRepo.countByStatus("APPROVED"); // Tin đang bán
    }

    public long countPendingPosts() {
        return postRepo.countByStatus("PENDING"); // Tin chờ duyệt
    }

    public long countPendingComplaints() {
        return complaintRepo.countByStatus("PENDING"); // Khiếu nại chưa xử lý
    }
    // --- QUẢN LÝ USER ---
    public List<User> getRecentUsers() {
        return userRepo.findTop5ByOrderByIdDesc();
    }

    // ==========================================
    // 2. QUẢN LÝ TIN ĐĂNG (KIỂM DUYỆT)
    // ==========================================

    // Lấy TOÀN BỘ bài đăng (kể cả cũ, mới, vi phạm) để hiển thị lịch sử
    public List<Post> getAllPosts() {
        return postRepo.findAllByOrderByCreatedAtDesc();
    }

    // Hành động 1: DUYỆT BÀI (Chuyển thành APPROVED -> Hiện lên trang chủ)
    public void approvePost(Integer postId) {
        Post post = postRepo.findById(postId).orElse(null);
        if (post != null) {
            post.setStatus("APPROVED");
            postRepo.save(post);
        }
    }

    // Hành động 2: TỪ CHỐI / BÁO VI PHẠM (Chuyển thành REJECTED -> Hiện chữ Vi phạm)
    // Lưu ý: Không delete() để giữ lại bằng chứng trong Admin
    public void rejectPost(Integer postId) {
        Post post = postRepo.findById(postId).orElse(null);
        if (post != null) {
            post.setStatus("REJECTED");
            postRepo.save(post);
        }
    }

    // ==========================================
    // 3. XỬ LÝ KHIẾU NẠI (COMPLAINTS)
    // ==========================================

    // Lấy danh sách khiếu nại
    public List<Complaint> getAllComplaints() {
        return complaintRepo.findAllByOrderByCreatedAtDesc();
    }

    // TÌNH HUỐNG 1: Báo cáo sai -> Giữ bài viết, Đóng khiếu nại
    public void dismissComplaint(Integer id) {
        Complaint c = complaintRepo.findById(id).orElse(null);
        if (c != null) {
            c.setStatus("RESOLVED"); // Đã xử lý xong
            complaintRepo.save(c);
        }
    }

    // TÌNH HUỐNG 2: Báo cáo đúng -> Đánh dấu bài là VI PHẠM, Đóng khiếu nại
    public void acceptComplaint(Integer id) {
        Complaint c = complaintRepo.findById(id).orElse(null);
        if (c != null && c.getPost() != null) {
            // 1. Xử lý bài viết: Chuyển sang REJECTED (Vi phạm)
            Post p = c.getPost();
            p.setStatus("REJECTED");
            postRepo.save(p);

            // 2. Xử lý khiếu nại: Đánh dấu đã xong
            c.setStatus("RESOLVED");
            complaintRepo.save(c);
        }
    }
}