package com.example.demo.service;

import com.example.demo.entity.ContestRanking;
import com.example.demo.entity.ContestantProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ContestRankingRepository;
import com.example.demo.repository.ContestantProfileRepository;
import com.example.demo.repository.ProgrammingContestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private final ContestantProfileRepository profileRepository;
    private final ContestRankingRepository rankingRepository;
    private final ProgrammingContestRepository contestRepository;

    public LeaderboardService(ContestantProfileRepository profileRepository,
                              ContestRankingRepository rankingRepository,
                              ProgrammingContestRepository contestRepository) {
        this.profileRepository = profileRepository;
        this.rankingRepository = rankingRepository;
        this.contestRepository = contestRepository;
    }

    public List<ContestantProfile> getGlobalLeaderboard() {
        List<ContestantProfile> profiles = profileRepository.findAll();
        profiles.sort(Comparator.comparingInt(ContestantProfile::getTotalPoints).reversed());

        AtomicInteger rank = new AtomicInteger(1);
        return profiles.stream()
                .peek(p -> p.setGlobalRank(rank.getAndIncrement()))
                .collect(Collectors.toList());
    }

    public List<ContestRanking> getContestLeaderboard(Long contestId) {
        if (!contestRepository.existsById(contestId)) {
            throw new ResourceNotFoundException("Contest not found with ID: " + contestId);
        }

        List<ContestRanking> rankings = new ArrayList<>();
        for (ContestRanking r : rankingRepository.findAll()) {
            if (r.getContest().getId().equals(contestId)) {
                rankings.add(r);
            }
        }

        rankings.sort(Comparator.comparingInt(ContestRanking::getCurrentScore).reversed()
                .thenComparingInt(ContestRanking::getPenaltyTime));

        AtomicInteger rank = new AtomicInteger(1);
        return rankings.stream()
                .peek(r -> r.setLocalRank(rank.getAndIncrement()))
                .collect(Collectors.toList());
    }
}
