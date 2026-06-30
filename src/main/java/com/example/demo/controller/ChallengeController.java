package com.example.demo.controller;

import com.example.demo.entity.CodingChallenge;
import com.example.demo.service.ChallengeService;
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
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping
    public ResponseEntity<List<CodingChallenge>> getAllChallenges() {
        return ResponseEntity.ok(challengeService.getAllChallenges());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodingChallenge> getChallengeById(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.getChallengeById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROBLEM_SETTER', 'PLATFORM_ADMIN')")
    public ResponseEntity<String> createChallenge(@RequestBody CodingChallenge challenge, Principal principal) {
        challengeService.createChallenge(challenge, principal.getName());
        return ResponseEntity.ok("Challenge created");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROBLEM_SETTER', 'PLATFORM_ADMIN')")
    public ResponseEntity<String> updateChallenge(@PathVariable Long id, @RequestBody CodingChallenge challenge) {
        challengeService.updateChallenge(id, challenge);
        return ResponseEntity.ok("Challenge updated");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<String> deleteChallenge(@PathVariable Long id) {
        challengeService.deleteChallenge(id);
        return ResponseEntity.ok("Challenge deleted");
    }
}
