package com.example.service.impl;

import com.example.model.PasswordResetToken;
import com.example.model.User;
import com.example.repository.PasswordResetTokenRepository;
import com.example.repository.UserRepository;
import com.example.service.EmailService;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // ====================================================
    // 1. LOAD USER CHO SPRING SECURITY
    // ====================================================
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    // ====================================================
    // ✅ TRIỂN KHAI HÀM findByUsername (Fix lỗi Controller)
    // ====================================================
    @Override
    public User findByUsername(String username) {
        // Trong hệ thống này, username chính là email
        return userRepository.findByEmail(username).orElse(null);
    }

    // ====================================================
    // 2. ĐĂNG KÝ
    // ====================================================
    @Override
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) user.setRole("USER");
        userRepository.save(user);
    }

    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) user.setRole("USER");
        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    // ====================================================
    // 3. LOGIN
    // ====================================================
    @Override
    public Optional<User> loginByEmail(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        return userOpt.filter(user ->
                passwordEncoder.matches(password, user.getPassword())
        );
    }

    @Override
    public Optional<User> loginByPhone(String phone, String password) {
        Optional<User> userOpt = userRepository.findByPhone(phone);

        return userOpt.filter(user ->
                passwordEncoder.matches(password, user.getPassword())
        );
    }

    @Override
    public User socialLogin(String email, String name) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setName(name);
                    u.setEmail(email);
                    u.setPassword(passwordEncoder.encode("default"));
                    u.setRole("USER");
                    return userRepository.save(u);
                });
    }

    // ====================================================
    // 4. REQUEST RESET PASSWORD
    // ====================================================
    @Override
    public boolean requestPasswordReset(String email) {

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty())
            return false;

        passwordResetTokenRepository.deleteByEmail(email);

        String token = UUID.randomUUID().toString();

        PasswordResetToken prt = new PasswordResetToken();
        prt.setToken(token);
        prt.setEmail(email);
        prt.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        passwordResetTokenRepository.save(prt);

        String resetUrl = "http://localhost:8080/reset-password?token=" + token;

        String content = """
                Xin chào,
                Nhấn vào liên kết sau để đặt lại mật khẩu (hiệu lực 30 phút):
                %s
                """.formatted(resetUrl);

        emailService.send(email, "Đặt lại mật khẩu - EV Trading", content);

        return true;
    }

    // ====================================================
    // 5. RESET PASSWORD
    // ====================================================
    @Override
    public boolean resetPassword(String token, String newPassword) {

        Optional<PasswordResetToken> opt = passwordResetTokenRepository.findByToken(token);
        if (opt.isEmpty())
            return false;

        PasswordResetToken prt = opt.get();

        if (prt.isExpired()) {
            passwordResetTokenRepository.delete(prt);
            return false;
        }

        User user = userRepository.findByEmail(prt.getEmail())
                .orElse(null);

        if (user == null) {
            passwordResetTokenRepository.delete(prt);
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(prt);

        return true;
    }

    // ====================================================
    // 6. FIND USER
    // ====================================================
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