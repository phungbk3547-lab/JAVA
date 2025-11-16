package com.example.service;

import com.example.model.Post;
import com.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepo;

    public PostService(PostRepository postRepo) {
        this.postRepo = postRepo;
    }

    // Lấy 20 tin mới nhất hiển thị trang chủ / bảng tin
    public List<Post> getLatestPosts() {
        return postRepo.findTop20ByOrderByCreatedAtDesc();
    }

    // Lưu / cập nhật tin
    public Post save(Post post) {
        return postRepo.save(post);
    }

    // Lấy toàn bộ (nếu cần)
    public List<Post> findAll() {
        return postRepo.findAll();
    }

    // Lấy chi tiết theo id
    public Post findById(Integer id) {
        return postRepo.findById(id).orElse(null);
    }

    // Xoá tin
    public void delete(Integer id) {
        postRepo.deleteById(id);
    }
}
