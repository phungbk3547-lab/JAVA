package com.example.controller;

import com.example.repository.PostRepository;
import com.example.model.Post;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final PostRepository postRepo;

    public HomeController(PostRepository postRepo) {
        this.postRepo = postRepo;
    }

    @GetMapping("/")
    public String home(HttpSession session,
                       Model model,
                       @RequestParam(required = false) String q,
                       @RequestParam(required = false) String type) {

        model.addAttribute("user", session.getAttribute("user"));

        // Lấy 20 tin mới nhất từ DB
        List<Post> posts = postRepo.findTop20ByOrderByCreatedAtDesc();

        // Lọc theo từ khóa (tiêu đề hoặc địa điểm)
        if (q != null && !q.isBlank()) {
            String kw = q.toLowerCase();
            posts = posts.stream()
                    .filter(p ->
                            (p.getTitle() != null && p.getTitle().toLowerCase().contains(kw)) ||
                                    (p.getLocation() != null && p.getLocation().toLowerCase().contains(kw))
                    )
                    .collect(Collectors.toList());
        }

        // Lọc theo loại tin (EV / PIN)
        if (type != null && !type.isBlank()) {
            String t = type.toUpperCase();
            posts = posts.stream()
                    .filter(p -> p.getCategory() != null &&
                            p.getCategory().equalsIgnoreCase(t))
                    .collect(Collectors.toList());
        }

        model.addAttribute("posts", posts);
        model.addAttribute("q", q);
        model.addAttribute("type", type);

        return "index";
    }

    @GetMapping("/about")
    public String about() { return "about"; }

    @GetMapping("/market")
    public String market() { return "market"; }

    @GetMapping("/battery")
    public String battery() { return "battery"; }
}
