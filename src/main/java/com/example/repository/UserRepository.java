package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {  // ⭐ ĐỔI Ở ĐÂY
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByPhoneAndPassword(String phone, String password);
}
