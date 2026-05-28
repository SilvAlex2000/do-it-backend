package com.example.doit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public User registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Async
    public void sendVerificationEmailAsync(String to, String token) {
        emailService.sendVerificationEmail(to, token);
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }
}