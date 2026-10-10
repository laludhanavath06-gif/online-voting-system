package com.codec.voting.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** One row per cast ballot. Unique constraint on voter_id makes double-voting impossible at DB level. */
@Entity
@Table(name = "votes", uniqueConstraints = @UniqueConstraint(columnNames = "voter_id"))
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "voter_id")
    private AppUser voter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    private LocalDateTime castAt = LocalDateTime.now();

    public Long getId() { return id; }
    public AppUser getVoter() { return voter; }
    public void setVoter(AppUser voter) { this.voter = voter; }
    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }
    public LocalDateTime getCastAt() { return castAt; }
    public void setCastAt(LocalDateTime castAt) { this.castAt = castAt; }
}
