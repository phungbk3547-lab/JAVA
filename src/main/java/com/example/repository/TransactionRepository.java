package com.example.repository;

import com.example.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Chỉ cần khai báo là Interface
// extends JpaRepository <Tên Model, Kiểu dữ liệu của ID>
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Xong! Không cần viết gì thêm.
    // Tự nhiên bạn sẽ có sẵn các hàm: .save(), .delete(), .findById()... để dùng.

}