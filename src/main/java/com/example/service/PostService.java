package com.example.service;

import com.example.model.Post;
import com.example.model.User;
import com.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepo;

    public PostService(PostRepository postRepo) {
        this.postRepo = postRepo;
    }

    /* ================== BẢNG TIN / TRANG CHỦ ================== */

    // Lấy 20 tin mới nhất hiển thị trang chủ / bảng tin
    public List<Post> getLatestPosts() {
        return postRepo.findTop20ByOrderByCreatedAtDesc();
    }

    // ⭐ Tìm kiếm / lọc theo loại (type) + từ khóa (q)
    // type: "EV", "PIN" hoặc rỗng
    // q: tiêu đề cần tìm (có thể rỗng)
    public List<Post> searchPosts(String type, String q) {
        String category = (type == null) ? "" : type.trim();
        String keyword  = (q == null) ? "" : q.trim();

        // Không nhập gì → 20 tin mới nhất
        if (category.isEmpty() && keyword.isEmpty()) {
            return getLatestPosts();
        }

        // Chỉ lọc theo loại
        if (!category.isEmpty() && keyword.isEmpty()) {
            return postRepo.findByCategoryContainingIgnoreCase(category);
        }

        // Chỉ lọc theo từ khoá tiêu đề
        if (category.isEmpty()) {
            return postRepo.findByTitleContainingIgnoreCase(keyword);
        }

        // Có cả hai → AND: đúng loại + chứa keyword trong title
        return postRepo.findByCategoryContainingIgnoreCaseAndTitleContainingIgnoreCase(
                category, keyword
        );
    }

    /* ================== CRUD CƠ BẢN ================== */

    // Lưu / cập nhật tin
    public Post save(Post post) {
        return postRepo.save(post);
    }

    // Lấy toàn bộ (nếu cần)
    public List<Post> findAll() {
        return postRepo.findAll();
    }

    // Lấy chi tiết theo id – trả về Optional (dùng cho orElseThrow, etc.)
    public Optional<Post> findById(Integer id) {
        return postRepo.findById(id);
    }

    // Bản tiện dụng: trả về Post hoặc null (tương đương bản cũ)
    public Post findByIdOrNull(Integer id) {
        return postRepo.findById(id).orElse(null);
    }

    // Xoá tin theo id
    public void deleteById(Integer id) {
        postRepo.deleteById(id);
    }

    // Giữ tên delete như bản cũ, gọi lại deleteById
    public void delete(Integer id) {
        deleteById(id);
    }

    /* ================== TIN THEO NGƯỜI DÙNG ================== */

    // Lấy danh sách tin của 1 user (dành cho trang "Tin của tôi")
    public List<Post> findByUser(User user) {
        return postRepo.findByUserOrderByCreatedAtDesc(user);
    }
}
