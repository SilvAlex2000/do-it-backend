package com.example.doit;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        String apiKey = System.getenv("RESEND_API_KEY");
        Resend resend = new Resend(apiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(to)
                .subject("Confirm Your Account")
                .html("<p>Click <a href='https://do-it-backend-6a5i.onrender.com/api/verify/" + token + "'>here</a> to verify.</p>")
                .build();

        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }
}