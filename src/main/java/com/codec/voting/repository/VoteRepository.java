package com.codec.voting.repository;

import com.codec.voting.model.Candidate;
import com.codec.voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    long countByCandidate(Candidate candidate);
    boolean existsByVoterId(Long voterId);
}
