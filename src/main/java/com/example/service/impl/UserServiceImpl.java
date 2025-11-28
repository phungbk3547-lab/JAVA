package com.example.service.impl;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService; // ✅ Đổi từ AuthService thành UserService
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// ✅ Triển khai Interface chính (UserService đã extends UserDetailsService)
@Service
public class UserServiceImpl implements UserService { // ✅ Đổi tên Class

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, Long> resetTokens = new ConcurrentHashMap<>(); // Giữ lại logic reset password

    // ✅ Constructor Injection
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =======================================================
    // 🎯 loadUserByUsername (SPRING SECURITY CORE)
    // Được gọi khi người dùng cố gắng đăng nhập
    // =======================================================
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword()) // PHẢI LÀ HASHED PASSWORD
                .roles(user.getRole())
                .build();
    }

    // =======================================================
    // 1. CHỨC NĂNG ĐĂNG KÝ/LƯU (Đã thêm Mã hóa Mật khẩu)
    // =======================================================

    // Phương thức register của Thành viên 1, dùng logic mã hóa của bạn
    @Override
    public void register(User user) {
        // ⭐ BẮT BUỘC: Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        userRepository.save(user);
    }

    // Phương thức save() của bạn (Dùng cho việc cập nhật user/lưu chung)
    @Override
    public User save(User user) {
        // Giữ nguyên: Chức năng này không nên tự mã hóa nếu user đã có ID (đang update)
        // Tuy nhiên, ta giữ lại để tương thích với các Controller cũ (nếu có)
        return userRepository.save(user);
    }

    // Phương thức này có trong AuthController cũ, giữ lại để biên dịch
    @Override
    public User registerUser(User user) {
        // Gọi hàm register(void) để đảm bảo mật khẩu được mã hóa
        register(user);
        return user; // Hàm register là void, nhưng ta cần trả về User cho Controller
    }

    // =======================================================
    // 2. LOGIC ĐĂNG NHẬP THỦ CÔNG (Giữ lại cho biên dịch nhưng KHÔNG SỬ DỤNG)
    // =======================================================
    @Override
    public Optional<User> loginByEmail(String email, String password) {
        throw new UnsupportedOperationException("Spring Security handles authentication.");
    }

    @Override
    public Optional<User> loginByPhone(String phone, String password) {
        throw new UnsupportedOperationException("Spring Security handles authentication.");
    }

    @Override
    public User socialLogin(String email, String name) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            // ✅ Ở ĐÂY TRƯỚC ĐÓ LÀ setUsername(name)
            // NHƯNG Entity User KHÔNG CÓ username NỮA → DÙNG name:
            u.setName(name);
            u.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // ✅ Mã hóa
            u.setRole("USER");
            return userRepository.save(u);
        });
    }

    // =======================================================
    // 3. LOGIC RESET MẬT KHẨU (Đã thêm Mã hóa Mật khẩu)
    // =======================================================
    @Override
    public boolean requestPasswordReset(String email) {
        if (!StringUtils.hasText(email)) return false;

        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;

        var user = userOpt.get();
        String token = UUID.randomUUID().toString();
        resetTokens.put(token, user.getId());

        System.out.println("[RESET] Token for " + email + ": " + token);
        return true;
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        if (!StringUtils.hasText(token) || !StringUtils.hasText(newPassword)) return false;

        Long userId = resetTokens.get(token);
        if (userId == null) return false;

        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return false;

        var user = userOpt.get();

        // ⭐ BẮT BUỘC: Mã hóa mật khẩu mới trước khi lưu
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetTokens.remove(token);
        return true;
    }

    // =======================================================
    // 4. CÁC PHƯƠNG THỨC TÌM KIẾM
    // =======================================================
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
