package com.example.doit;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService,
                          UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (password == null || !password.matches(passwordRegex)) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Password too weak."));
        }

        try {
            if (userRepository.findByUsername(username).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Username taken"));
            }

            userService.registerUser(username, password); // Email removed
            return ResponseEntity.ok(Map.of("status", "success", "message", "Registration successful!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Registration failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data, HttpSession session) {
        return userService.login(data.get("username"), data.get("password"))
                .map(user -> {
                    session.setAttribute("user", user.getUsername());
                    return ResponseEntity.ok(Map.of("status", "success", "message", "Logged in!"));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("status", "error", "message", "Invalid credentials")));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("status", "success", "message", "Logged out"));
    }

    @GetMapping("/check-auth")
    public Map<String, Object> checkAuth(HttpSession session) {
        String username = (String) session.getAttribute("user");
        return Map.of("is_logged_in", username != null, "user", username == null ? "" : username);
    }
}