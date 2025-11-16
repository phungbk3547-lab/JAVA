package com.example.service;

import com.example.model.User;
import java.util.Optional;

public interface AuthService {
    // các hàm bạn đã có
    void register(User user);
    Optional<User> loginByEmail(String email, String password);
    Optional<User> loginByPhone(String phone, String password);
    User socialLogin(String email, String name);

    // >>> THÊM 2 HÀM NÀY <<<
    boolean requestPasswordReset(String email);
    boolean resetPassword(String token, String newPassword);
}
