package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")   // ⭐ Bảng trong DB là "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;      // Có thể dùng Integer cũng được, nhưng Long là lựa chọn phổ biến

    @Column(nullable = false, length = 100)
    private String name;  // Tên hiển thị / full name

    @Column(nullable = false, unique = true, length = 100)
    private String email; // Dùng để đăng nhập / định danh

    @Column(unique = true, length = 20)
    private String phone; // Số điện thoại (có thể null)

    @Column(nullable = false, length = 100)
    private String password; // ⭐ Lưu HASH mật khẩu (BCrypt)

    @Column(nullable = false, length = 20)
    private String role = "USER";   // ⭐ Mặc định USER

    // ===== CONSTRUCTOR BẮT BUỘC CHO JPA =====
    public User() {
    }

    // (Tuỳ chọn) Constructor tiện dụng
    public User(String name, String email, String phone, String password, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    // ===== GETTER / SETTER =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // =======================================================
    // ⭐ HỖ TRỢ getFullName / setFullName cho code cũ (nếu có)
    // =======================================================
    @Transient
    public String getFullName() {
        // Dùng luôn name làm fullName
        return this.name;
    }

    @Transient
    public void setFullName(String fullName) {
        this.name = fullName;
    }
}
