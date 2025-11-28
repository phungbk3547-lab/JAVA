package com.example.controller;

import com.example.model.Post;
import com.example.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping({"/", "/home"})
    public String home(@RequestParam(name = "q", required = false) String q,
                       @RequestParam(name = "type", required = false) String type,
                       Model model) {

        List<Post> posts = postService.searchPosts(type, q);

        model.addAttribute("posts", posts);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("type", type == null ? "" : type);

        return "member/home";
    }
}
