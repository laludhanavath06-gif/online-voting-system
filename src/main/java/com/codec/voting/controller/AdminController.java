package com.codec.voting.controller;

import com.codec.voting.model.Candidate;
import com.codec.voting.repository.CandidateRepository;
import com.codec.voting.repository.UserRepository;
import com.codec.voting.repository.VoteRepository;
import com.codec.voting.service.VotingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository users;
    private final CandidateRepository candidates;
    private final VoteRepository votes;
    private final VotingService service;

    public AdminController(UserRepository users, CandidateRepository candidates,
                           VoteRepository votes, VotingService service) {
        this.users = users;
        this.candidates = candidates;
        this.votes = votes;
        this.service = service;
    }

    @GetMapping
    public String panel(Model model) {
        long voters = users.countByRole("ROLE_VOTER");
        long voted = users.countByRoleAndHasVotedTrue("ROLE_VOTER");
        model.addAttribute("election", service.getElection());
        model.addAttribute("candidates", candidates.findAll());
        model.addAttribute("voters", users.findByRoleOrderByFullNameAsc("ROLE_VOTER"));
        model.addAttribute("totalVoters", voters);
        model.addAttribute("totalVoted", voted);
        model.addAttribute("turnout", voters == 0 ? 0 : (int) Math.round(voted * 100.0 / voters));
        return "admin/panel";
    }

    @PostMapping("/candidates")
    public String addCandidate(@RequestParam String name, @RequestParam String party,
                               @RequestParam(required = false) String manifesto, RedirectAttributes ra) {
        if (name.isBlank() || party.isBlank()) {
            ra.addFlashAttribute("error", "Candidate name and party are required.");
        } else if (votes.count() > 0) {
            ra.addFlashAttribute("error", "Cannot add candidates after voting has started.");
        } else {
            Candidate c = new Candidate();
            c.setName(name.trim());
            c.setParty(party.trim());
            c.setManifesto(manifesto);
            candidates.save(c);
            ra.addFlashAttribute("success", "Candidate added.");
        }
        return "redirect:/admin";
    }

    @PostMapping("/candidates/{id}/delete")
    public String deleteCandidate(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.deleteCandidate(id);
            ra.addFlashAttribute("success", "Candidate removed.");
        } catch (IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/election/toggle")
    public String toggle(RedirectAttributes ra) {
        boolean open = !service.getElection().isVotingOpen();
        if (open && candidates.count() < 2) {
            ra.addFlashAttribute("error", "Add at least two candidates before opening the election.");
            return "redirect:/admin";
        }
        service.setVotingOpen(open);
        ra.addFlashAttribute("success", open ? "Voting is now OPEN." : "Voting is now CLOSED. Results are published.");
        return "redirect:/admin";
    }
}
