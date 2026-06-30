package com.example.demo.controller;

import com.example.demo.entity.ProgrammingContest;
import com.example.demo.service.ContestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/contests")
public class ContestController {

    private final ContestService contestService;

    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @GetMapping
    public ResponseEntity<List<ProgrammingContest>> getAllContests() {
        return ResponseEntity.ok(contestService.getAllContests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgrammingContest> getContestById(@PathVariable Long id) {
        return ResponseEntity.ok(contestService.getContestById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<String> createContest(@RequestBody ProgrammingContest contest) {
        contestService.createContest(contest);
        return ResponseEntity.ok("Contest created");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<String> updateContest(@PathVariable Long id, @RequestBody ProgrammingContest contest) {
        contestService.updateContest(id, contest);
        return ResponseEntity.ok("Contest updated");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<String> deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
        return ResponseEntity.ok("Contest deleted");
    }

    @PostMapping("/{id}/enroll")
    @PreAuthorize("hasRole('CONTESTANT')")
    public ResponseEntity<String> enrollParticipant(@PathVariable Long id, Principal principal) {
        contestService.enrollParticipant(id, principal.getName());
        return ResponseEntity.ok("Enrolled successfully");
    }
}
