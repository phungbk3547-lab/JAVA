package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Tiêu đề tin
    @Column(nullable = false)
    private String title;

    // Nội dung mô tả – map sang cột description NVARCHAR(MAX)
    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String content;

    // Giá
    private BigDecimal price;

    // Địa điểm (HomeController / filter location dùng field này)
    private String location;

    // Loại tin / loại xe – map sang cột type
    @Column(name = "type")
    private String category;

    // Ngày đăng – map sang cột post_date
    @Column(name = "post_date")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Ảnh – map sang cột image_path
    @Column(name = "image_path")
    private String imagePath;

    // Trạng thái tin: AVAILABLE / SOLD / PENDING ...
    // Có default ở DB là AVAILABLE (nếu dùng schema auto-generate)
    @Column(name = "status", columnDefinition = "NVARCHAR(50) DEFAULT 'AVAILABLE'")
    private String status = "AVAILABLE";

    // Tên người đăng – chỉ dùng để hiển thị, không lưu DB
    @Transient
    private String authorName;

    // Quan hệ với User qua cột user_id
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // ===== GETTERS / SETTERS =====

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
