package com.example.repository;

import com.example.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findTop20ByOrderByCreatedAtDesc();

    Page<Post> findByCategoryContainingIgnoreCaseOrTitleContainingIgnoreCase(
            String category,
            String title,
            Pageable pageable
    );
}
