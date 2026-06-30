package com.example.demo.controller;

import com.example.demo.entity.ChallengeSubmission;
import com.example.demo.service.ProfileService;
import com.example.demo.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final ProfileService profileService;

    public SubmissionController(SubmissionService submissionService, ProfileService profileService) {
        this.submissionService = submissionService;
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<List<ChallengeSubmission>> getAllSubmissions() {
        return ResponseEntity.ok(submissionService.getAllSubmissions());
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CONTESTANT')")
    public ResponseEntity<List<ChallengeSubmission>> getMySubmissions(Authentication authentication) {
        Long profileId = profileService.getProfileByUsername(authentication.getName()).getId();
        return ResponseEntity.ok(submissionService.getSubmissionsByContestant(profileId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    public ResponseEntity<String> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.ok("Submission deleted");
    }

    @PostMapping("/{challengeId}/submit")
    @PreAuthorize("hasRole('CONTESTANT')")
    public ResponseEntity<ChallengeSubmission> submit(@PathVariable Long challengeId,
                                                       @RequestBody Map<String, Object> body,
                                                       Principal principal) {
        String sourceCode = (String) body.get("sourceCode");
        Long contestId = body.get("contestId") != null ? Long.valueOf(body.get("contestId").toString()) : null;
        ChallengeSubmission submission = submissionService.processSubmission(challengeId, contestId, sourceCode, principal.getName());
        return ResponseEntity.ok(submission);
    }
}
