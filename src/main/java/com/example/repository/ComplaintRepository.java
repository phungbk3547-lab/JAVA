package com.example.repository;

import com.example.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
    List<Complaint> findByStatus(String status);
    long countByStatus(String status);
    List<Complaint> findAllByOrderByCreatedAtDesc();
}