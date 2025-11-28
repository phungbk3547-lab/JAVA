package com.example.service.impl;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================================
    // 1. ĐĂNG KÝ
    // =========================================
    @Override
    public void register(User user) {
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        // mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    // =========================================
    // 2. LOGIN BẰNG EMAIL
    // =========================================
    @Override
    public Optional<User> loginByEmail(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }

    // =========================================
    // 3. LOGIN BẰNG SỐ ĐIỆN THOẠI
    // =========================================
    @Override
    public Optional<User> loginByPhone(String phone, String rawPassword) {
        return userRepository.findByPhone(phone)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }

    // =========================================
    // 4. SOCIAL LOGIN (FAKE)
    // =========================================
    @Override
    public User socialLogin(String email, String name) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setName(name); // entity không còn username → dùng name
                    u.setRole("USER");
                    u.setPassword(passwordEncoder.encode("oauth_login"));
                    return userRepository.save(u);
                });
    }

    // =========================================
    // 5. QUÊN / RESET MẬT KHẨU (BẢN ĐƠN GIẢN)
    // =========================================
    @Override
    public boolean requestPasswordReset(String email) {
        // tạm thời chỉ kiểm tra email có tồn tại
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        // chưa cài token thật, tạm thời luôn false
        return false;
    }
}
