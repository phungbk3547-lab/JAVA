package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 Tìm theo email (dùng cho Spring Security)
    Optional<User> findByEmail(String email);

    // 🔍 Tìm theo số điện thoại (nếu cần)
    Optional<User> findByPhone(String phone);

    // 🔍 Lấy 5 user mới nhất (nếu cần hiển thị danh sách)
    List<User> findTop5ByOrderByIdDesc();
}
