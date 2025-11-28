package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết với bài đăng (xe điện) - Lấy từ nhóm 2
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // Người mua - Lấy từ nhóm 1
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    private Double amount; // Số tiền thanh toán
    private String paymentMethod; // VD: COD, Banking
    private String status; // VD: PENDING, COMPLETED, CANCELLED
    private LocalDateTime transactionDate;

    // Constructor, Getters, Setters...
}