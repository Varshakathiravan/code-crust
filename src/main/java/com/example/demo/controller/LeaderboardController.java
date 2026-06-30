package com.example.demo.controller;

import com.example.demo.entity.ContestRanking;
import com.example.demo.entity.ContestantProfile;
import com.example.demo.service.LeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/global")
    public ResponseEntity<List<ContestantProfile>> getGlobalLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getGlobalLeaderboard());
    }

    @GetMapping("/contest/{contestId}")
    public ResponseEntity<List<ContestRanking>> getContestLeaderboard(@PathVariable Long contestId) {
        return ResponseEntity.ok(leaderboardService.getContestLeaderboard(contestId));
    }
}
