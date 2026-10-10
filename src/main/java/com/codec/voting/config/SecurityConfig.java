package com.codec.voting.config;
  import jakarta.servlet.DispatcherType;

import com.codec.voting.model.AppUser;
import com.codec.voting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository users) {
        return email -> {
            AppUser u = users.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("No account for " + email));
            return new User(u.getEmail(), u.getPassword(), List.of(new SimpleGrantedAuthority(u.getRole())));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
  .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                .requestMatchers("/", "/login", "/register", "/css/**", "/error").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error")
                .permitAll())
            .logout(l -> l.logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }

    /** Creates the default admin on first run. */
    @Bean
    public CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder encoder,
                                       @Value("${app.admin.email}") String email,
                                       @Value("${app.admin.password}") String password) {
        return args -> {
            if (!users.existsByEmail(email)) {
                AppUser admin = new AppUser();
                admin.setFullName("Election Administrator");
                admin.setEmail(email);
                admin.setPassword(encoder.encode(password));
                admin.setRole("ROLE_ADMIN");
                users.save(admin);
            }
        };
    }
}
