package com.codec.voting.controller;

import com.codec.voting.model.AppUser;
import com.codec.voting.repository.UserRepository;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String fullName,
                           @RequestParam String email,
                           @RequestParam String voterId,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {
        String error = null;
        email = email.trim().toLowerCase();
        voterId = voterId.trim().toUpperCase();
        if (fullName.isBlank()) error = "Full name is required.";
        else if (password.length() < 6) error = "Password must be at least 6 characters.";
        else if (!password.equals(confirmPassword)) error = "Passwords do not match.";
        else if (Period.between(dateOfBirth, LocalDate.now()).getYears() < 18) error = "You must be at least 18 years old to register.";
        else if (users.existsByEmail(email)) error = "An account with this email already exists.";
        else if (users.existsByVoterId(voterId)) error = "This Voter ID is already registered.";

        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("fullName", fullName);
            model.addAttribute("email", email);
            model.addAttribute("voterId", voterId);
            return "register";
        }
        AppUser u = new AppUser();
        u.setFullName(fullName.trim());
        u.setEmail(email);
        u.setVoterId(voterId);
        u.setDateOfBirth(dateOfBirth);
        u.setPassword(encoder.encode(password));
        u.setRole("ROLE_VOTER");
        users.save(u);
        return "redirect:/login?registered";
    }
}
