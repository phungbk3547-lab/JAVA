package com.example.service;

import com.example.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    // Các hàm của Thành viên 1:
    void register(User user);
    Optional<User> loginByEmail(String email, String password);
    Optional<User> loginByPhone(String phone, String password);
    User socialLogin(String email, String name);
    boolean requestPasswordReset(String email);
    boolean resetPassword(String token, String newPassword);

    // Các hàm của bạn:
    User save(User user);
    User registerUser(User user);
    User updateUser(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findById(Long id);

    // ✅ ĐÃ THÊM HÀM NÀY ĐỂ KHẮC PHỤC LỖI Ở CONTROLLER
    User findByUsername(String username);
}