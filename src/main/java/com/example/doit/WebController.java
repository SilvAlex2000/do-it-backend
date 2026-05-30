package com.example.doit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;
import org.springframework.http.ResponseEntity;

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
            default -> "404";
        };
    }

    @GetMapping("/api/profile/{username}")
    @ResponseBody
    public ResponseEntity<?> getPublicProfile(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(user -> ResponseEntity.ok(Map.of(
                        "username", user.getUsername(),
                        "profile_pic", "/" + user.getProfilePicPath()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/comment-item")
    public String getCommentTemplate() {
        return "comment_item";
    }
}
