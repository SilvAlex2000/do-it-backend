package com.example.doit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Controller
public class WebController {

    private final ResourceLoader resourceLoader;

    public WebController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping({"/", "/home", "/user", "/user_center", "/profile/{username}", "/post/{id}"})
    public String index() {
        return "index";
    }

    @GetMapping("/api/content/{page}/**")
    @ResponseBody
    public String getContent(@PathVariable String page) {
        String fileName = switch (page) {
            case "home" -> "home_content";
            case "login" -> "login_content";
            case "register" -> "register_content";
            case "post-item" -> "post_item";
            case "user_center" -> "personal_area_content";
            default -> "error_404";
        };
        return loadTemplate(fileName);
    }

    private String loadTemplate(String templateName) {
        try {
            Resource resource = resourceLoader.getResource("classpath:templates/" + templateName + ".html");
            try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                return FileCopyUtils.copyToString(reader);
            }
        } catch (Exception e) {
            return "<div>Error loading template</div>";
        }
    }
}
