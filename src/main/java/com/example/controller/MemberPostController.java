package com.example.controller;

import com.example.model.Post;
import com.example.model.User;
import com.example.service.PostService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/member")
public class MemberPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService; // Cần có UserService để lấy thông tin người đăng

    // Thư mục lưu ảnh tạm thời (để test)
    private final String UPLOAD_DIR = "src/main/resources/static/images/";

    // 1. HIỂN THỊ DANH SÁCH "TIN CỦA TÔI"
    // URL: http://localhost:8080/member/my-posts
    @GetMapping("/my-posts")
    public String showMyPosts(Model model, Principal principal) {
        // Nếu đã có đăng nhập (Principal != null), dùng findByUser
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username); // Cần method này trong UserService
            List<Post> posts = postService.findByUser(user);
            model.addAttribute("posts", posts);
        } else {
            // Nếu chưa đăng nhập (đang test), hiển thị toàn bộ
            // Gọi hàm findAll() khớp với PostService của bạn
            List<Post> posts = postService.findAll();
            model.addAttribute("posts", posts);
        }

        return "member/my_posts_list";
    }

    // 2. HIỂN THỊ FORM ĐĂNG TIN MỚI
    // URL: http://localhost:8080/member/posts/new
    @GetMapping("/posts/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "member/post_form";
    }

    // 3. HIỂN THỊ FORM CHỈNH SỬA
    @GetMapping("/posts/{id}/edit")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        // Dùng findById() khớp với PostService
        Optional<Post> post = postService.findById(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "member/post_form";
        }
        return "redirect:/member/my-posts";
    }

    // 4. XỬ LÝ LƯU TIN (TẠO MỚI / UPDATE)
    @PostMapping("/posts/save")
    public String savePost(@ModelAttribute("post") Post post,
                           @RequestParam(value = "image", required = false) MultipartFile image,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {
        try {
            // XỬ LÝ LƯU ẢNH (Nếu người dùng có chọn ảnh)
            if (image != null && !image.isEmpty()) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                // Lưu file vào thư mục static/images
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                // Set đường dẫn ảnh cho Post (để lưu vào DB)
                post.setImagePath("/images/" + fileName);
            } else {
                // Nếu đang sửa mà không chọn ảnh mới -> Giữ nguyên ảnh cũ (cần logic xử lý thêm nếu muốn chặt chẽ)
                // Hiện tại nếu null thì để null hoặc bạn có thể set ảnh mặc định
                if (post.getId() == null) {
                    post.setImagePath("/images/default-car.jpg");
                }
            }

            // GÁN NGƯỜI DÙNG (AUTHOR)
            if (principal != null) {
                User user = userService.findByUsername(principal.getName());
                post.setUser(user); // Map user vào bài viết
            }

            // GỌI SERVICE LƯU (Dùng hàm save() khớp với PostService)
            postService.save(post);

            redirectAttributes.addFlashAttribute("message", "Đăng tin thành công!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi khi upload ảnh!");
        }

        return "redirect:/member/my-posts";
    }

    // 5. XÓA TIN
    @PostMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable("postId") Integer postId,
                             RedirectAttributes redirectAttributes) {

        Optional<Post> postOptional = postService.findById(postId);

        if (postOptional.isPresent()) {
            // Dùng deleteById() khớp với PostService
            postService.deleteById(postId);
            redirectAttributes.addFlashAttribute("message", "Xóa tin thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy tin cần xóa.");
        }

        return "redirect:/member/my-posts";
    }
}