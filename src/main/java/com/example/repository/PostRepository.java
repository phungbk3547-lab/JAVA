package com.example.repository;

import com.example.model.Post;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // Lấy tất cả tin, sắp xếp theo ngày tạo mới nhất
    List<Post> findAllByOrderByCreatedAtDesc();

    // Đếm số tin theo status (AVAILABLE, SOLD, PENDING, ...)
    long countByStatus(String status);

    // Lấy danh sách tin theo status
    List<Post> findByStatus(String status);

    // Lấy 20 tin mới nhất (trang chủ / home dùng)
    List<Post> findTop20ByOrderByCreatedAtDesc();

    // Tìm kiếm phân trang theo category hoặc title (cho trang danh sách)
    Page<Post> findByCategoryContainingIgnoreCaseOrTitleContainingIgnoreCase(
            String category,
            String title,
            Pageable pageable
    );

    // Tìm LIST theo category (không phân trang)
    List<Post> findByCategoryContainingIgnoreCase(String category);

    // Tìm LIST theo title (không phân trang)
    List<Post> findByTitleContainingIgnoreCase(String title);

    // Tìm LIST theo cả category và title (AND)
    List<Post> findByCategoryContainingIgnoreCaseAndTitleContainingIgnoreCase(
            String category,
            String title
    );

    // Lấy tin theo user (chủ tin), sắp xếp mới nhất
    List<Post> findByUserOrderByCreatedAtDesc(User user);
}
