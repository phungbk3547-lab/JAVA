package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    // Lưu token -> userId (in-memory demo)
    // ⭐ DÙNG Integer cho khớp với User.id (INT trong DB)
    private final Map<String, Integer> resetTokens = new ConcurrentHashMap<>();

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ====== REGISTER / LOGIN / SOCIAL LOGIN ======
    @Override
    public void register(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> loginByEmail(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public Optional<User> loginByPhone(String phone, String password) {
        return userRepository.findByPhoneAndPassword(phone, password);
    }

    @Override
    public User socialLogin(String email, String name) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User u = new User();
            u.setEmail(email);
            u.setName(name);
            u.setPassword(UUID.randomUUID().toString()); // demo
            return userRepository.save(u);
        });
    }

    // ====== RESET PASSWORD (in-memory token) ======

    @Override
    public boolean requestPasswordReset(String email) {
        if (!StringUtils.hasText(email)) return false;

        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;

        var user = userOpt.get();

        // tạo token mới và lưu vào map
        String token = UUID.randomUUID().toString();
        resetTokens.put(token, user.getId());   // ⭐ user.getId() là Integer

        // Demo: in link ra console để bạn copy
        System.out.println("[RESET] Token for " + email + ": " + token);
        System.out.println("→ Open http://localhost:8080/reset-password?token=" + token);

        return true;
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        if (!StringUtils.hasText(token) || !StringUtils.hasText(newPassword)) return false;

        // ⭐ Lấy ra Integer, không phải Long
        Integer userId = resetTokens.get(token);
        if (userId == null) return false;

        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return false;

        var user = userOpt.get();
        user.setPassword(newPassword); // nếu có PasswordEncoder thì encode ở đây
        userRepository.save(user);

        // dùng xong xoá token để không reuse
        resetTokens.remove(token);
        return true;
    }
}
