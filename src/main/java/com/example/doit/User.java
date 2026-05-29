package com.example.doit;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_data")
public class User {
    @Id
    @Column(length = 100)
    private String username;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(length = 255)
    private String profilePicPath = "img/default-avatar.png";

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePicPath() { return profilePicPath; }
    public void setProfilePicPath(String profilePicPath) { this.profilePicPath = profilePicPath; }
}