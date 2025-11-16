package com.example.controller;

import com.example.model.Post;
import com.example.repository.PostRepository;
import com.example.service.FileStorageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepo;
    private final FileStorageService storage;

    public PostController(PostRepository postRepo, FileStorageService storage) {
        this.postRepo = postRepo;
        this.storage = storage;
    }

    // Danh sách + tìm kiếm
    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "9") int size,
                       @RequestParam(required = false) String q,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = (q == null || q.isBlank())
                ? postRepo.findAll(pageable)
                : postRepo.findByCategoryContainingIgnoreCaseOrTitleContainingIgnoreCase(q, q, pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("q", q);
        return "post_list";
    }

    // Form tạo mới
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "post_form";
    }

    // Lưu tạo mới
    @PostMapping
    public String create(@ModelAttribute Post post,
                         @RequestParam("image") MultipartFile image,
                         HttpSession session) throws Exception {
        String path = storage.store(image);
        post.setImagePath(path);

        // attach author từ session nếu có
        Object user = session.getAttribute("user");
        if (user != null) {
            try {
                String name = (String) user.getClass().getMethod("getName").invoke(user);
                post.setAuthorName(name);
            } catch (Exception ignore) {}
        }

        postRepo.save(post);
        return "redirect:/posts";
    }

    // Xem chi tiết
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Post post = postRepo.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "post_detail";
    }

    // Form sửa
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("post", postRepo.findById(id).orElseThrow());
        return "post_form";
    }

    // Lưu sửa
    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @ModelAttribute Post form,
                         @RequestParam("image") MultipartFile image) throws Exception {
        Post post = postRepo.findById(id).orElseThrow();
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setCategory(form.getCategory());
        post.setPrice(form.getPrice());

        if (image != null && !image.isEmpty()) {
            String path = storage.store(image);
            post.setImagePath(path);
        }

        postRepo.save(post);
        return "redirect:/posts/" + id;
    }

    // Xoá
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        postRepo.deleteById(id);
        return "redirect:/posts";
    }
}
