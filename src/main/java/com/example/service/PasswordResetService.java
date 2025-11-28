package com.example.service;

import com.example.model.PasswordResetToken;
import com.example.model.User;

import java.util.Optional;

public interface PasswordResetService {

    boolean sendResetLink(String email);

    Optional<PasswordResetToken> validateToken(String token);

    boolean resetPassword(String token, String newPassword);
}
