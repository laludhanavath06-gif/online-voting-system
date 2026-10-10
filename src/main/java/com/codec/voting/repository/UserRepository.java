package com.codec.voting.repository;

import com.codec.voting.model.AppUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByVoterId(String voterId);
    List<AppUser> findByRoleOrderByFullNameAsc(String role);
    long countByRole(String role);
    long countByRoleAndHasVotedTrue(String role);
}
