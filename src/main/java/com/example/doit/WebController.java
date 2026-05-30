package com.example.doit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

@Controller
public class WebController {

    private final UserRepository userRepository;

    public WebController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping({"/", "/home", "/user", "/user_center", "/profile/{username}", "/post/{id}"})
    public String index() {
        return "index";
    }

    @GetMapping("/api/content/{page}/**")
    @ResponseBody
    public String getContent(@PathVariable String page) {
        return switch (page) {
            case "home" -> "home_content";
            case "login" -> "login_content";
            case "register" -> "register_content";
            case "post-item" -> "post_item";
            case "user_center" -> "personal_area_content";
            default -> "error_404";
        };
    }

    @GetMapping("/api/profile/{username}")
    @ResponseBody
    public String getPublicProfile(@PathVariable String username, Model model) {
        return userRepository.findByUsername(username).map(user -> {
            model.addAttribute("target_username", user.getUsername());
            model.addAttribute("target_user_pic", "/" + user.getProfilePicPath());
            return "user_profile_public";
        }).orElse("error/404");
    }

    @GetMapping("/comment-item")
    public String getCommentTemplate() {
        return "comment_item";
    }
}
