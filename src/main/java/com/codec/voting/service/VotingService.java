package com.codec.voting.service;

import com.codec.voting.model.*;
import com.codec.voting.repository.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VotingService {

    public record ResultRow(Candidate candidate, long votes, double percent) {}

    private final UserRepository users;
    private final CandidateRepository candidates;
    private final VoteRepository votes;
    private final ElectionRepository elections;

    public VotingService(UserRepository users, CandidateRepository candidates,
                         VoteRepository votes, ElectionRepository elections) {
        this.users = users;
        this.candidates = candidates;
        this.votes = votes;
        this.elections = elections;
    }

    public Election getElection() {
        return elections.findById(1L).orElseGet(() -> elections.save(new Election()));
    }

    @Transactional
    public void setVotingOpen(boolean open) {
        Election e = getElection();
        e.setVotingOpen(open);
        elections.save(e);
    }

    @Transactional
    public void castVote(String voterEmail, Long candidateId) {
        if (!getElection().isVotingOpen()) {
            throw new IllegalStateException("Voting is currently closed.");
        }
        AppUser voter = users.findByEmail(voterEmail)
                .orElseThrow(() -> new IllegalStateException("Voter not found."));
        if (!"ROLE_VOTER".equals(voter.getRole())) {
            throw new IllegalStateException("Only registered voters can vote.");
        }
        if (voter.isHasVoted() || votes.existsByVoterId(voter.getId())) {
            throw new IllegalStateException("You have already cast your vote.");
        }
        Candidate c = candidates.findById(candidateId)
                .orElseThrow(() -> new IllegalStateException("Candidate not found."));
        Vote v = new Vote();
        v.setVoter(voter);
        v.setCandidate(c);
        votes.save(v);
        voter.setHasVoted(true);
        users.save(voter);
    }

    @Transactional
    public void deleteCandidate(Long id) {
        Candidate c = candidates.findById(id).orElseThrow(() -> new IllegalStateException("Candidate not found."));
        if (votes.countByCandidate(c) > 0) {
            throw new IllegalStateException("Cannot delete a candidate who already has votes.");
        }
        candidates.delete(c);
    }

    public List<ResultRow> results() {
        List<Candidate> all = candidates.findAll();
        long total = votes.count();
        List<ResultRow> rows = new ArrayList<>();
        for (Candidate c : all) {
            long n = votes.countByCandidate(c);
            rows.add(new ResultRow(c, n, total == 0 ? 0 : n * 100.0 / total));
        }
        rows.sort(Comparator.comparingLong(ResultRow::votes).reversed());
        return rows;
    }
}
