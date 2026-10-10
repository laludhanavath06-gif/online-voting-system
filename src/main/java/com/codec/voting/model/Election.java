package com.codec.voting.model;

import jakarta.persistence.*;

/** Single-row table (id = 1) holding election state. */
@Entity
@Table(name = "election")
public class Election {

    @Id
    private Long id = 1L;

    private String title = "General Election";

    private boolean votingOpen = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isVotingOpen() { return votingOpen; }
    public void setVotingOpen(boolean votingOpen) { this.votingOpen = votingOpen; }
}
