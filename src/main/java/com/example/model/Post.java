package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String content;

    private BigDecimal price;

    private String location;

    @Column(name = "type")
    private String category;

    @Column(name = "post_date")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ⭐ ẢNH (map sang cột image_path)
    @Column(name = "image_path")
    private String imagePath;

    // ⭐ TÊN NGƯỜI ĐĂNG (KHÔNG CÓ TRONG DB, nhưng bạn muốn dùng trong PostController)
    @Transient   // không map vào DB, tránh lỗi SQL
    private String authorName;

    // user_id (khớp với SQL Server của bạn)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ===== GETTERS / SETTERS =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    // ⭐ Getter/Setter AUTHOR NAME
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
