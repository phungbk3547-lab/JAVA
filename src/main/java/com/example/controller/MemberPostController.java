package com.example.controller;

import com.example.service.PostService;
import com.example.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/member")
public class MemberPostController {

    // ✅ FIX LỖI: Thêm injection cho PostService
    @Autowired
    private PostService postService;

    // ... Các phương thức khác (thêm bài, chỉnh sửa bài, xem bài của tôi)

    // XÓA BÀI ĐĂNG
    @PostMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable("postId") Long postId,
                             RedirectAttributes redirectAttributes) {

        Optional<Post> postOptional = postService.findById(postId);

        if (postOptional.isPresent()) {
            postService.deleteById(postId);
            redirectAttributes.addFlashAttribute("message", "Bài đăng đã được xóa thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy bài đăng để xóa.");
        }

        return "redirect:/member/posts";
    }
}