package com.codec.voting.controller;

import com.codec.voting.model.AppUser;
import com.codec.voting.repository.CandidateRepository;
import com.codec.voting.repository.UserRepository;
import com.codec.voting.service.VotingService;
import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VoterController {

    private final UserRepository users;
    private final CandidateRepository candidates;
    private final VotingService service;

    public VoterController(UserRepository users, CandidateRepository candidates, VotingService service) {
        this.users = users;
        this.candidates = candidates;
        this.service = service;
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Authentication auth, Model model) {
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (admin) return "redirect:/admin";
        AppUser me = users.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("me", me);
        model.addAttribute("election", service.getElection());
        model.addAttribute("candidates", candidates.findAll());
        return "dashboard";
    }

    @PostMapping("/vote")
    public String vote(@RequestParam Long candidateId, Principal principal, RedirectAttributes ra) {
        try {
            service.castVote(principal.getName(), candidateId);
            ra.addFlashAttribute("success", "Your vote has been recorded. Thank you for voting!");
        } catch (IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/results")
    public String results(Authentication auth, Model model) {
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean open = service.getElection().isVotingOpen();
        model.addAttribute("election", service.getElection());
        model.addAttribute("isAdmin", admin);
        if (open && !admin) {
            model.addAttribute("hidden", true);
        } else {
            var rows = service.results();
            model.addAttribute("rows", rows);
            model.addAttribute("totalVotes", rows.stream().mapToLong(r -> r.votes()).sum());
            if (!rows.isEmpty() && rows.get(0).votes() > 0) {
                boolean tie = rows.size() > 1 && rows.get(1).votes() == rows.get(0).votes();
                model.addAttribute("tie", tie);
                model.addAttribute("winner", rows.get(0).candidate());
            }
        }
        return "results";
    }
}
