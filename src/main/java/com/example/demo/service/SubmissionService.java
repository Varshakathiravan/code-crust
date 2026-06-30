package com.example.demo.service;

import com.example.demo.entity.ChallengeSubmission;
import com.example.demo.entity.ChallengeSubmission.Verdict;
import com.example.demo.entity.CodingChallenge;
import com.example.demo.entity.ContestRanking;
import com.example.demo.entity.ContestantProfile;
import com.example.demo.entity.ProgrammingContest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ChallengeSubmissionRepository;
import com.example.demo.repository.ContestRankingRepository;
import com.example.demo.repository.ContestantProfileRepository;
import com.example.demo.repository.ProgrammingContestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class SubmissionService {

    private final ChallengeSubmissionRepository submissionRepository;
    private final ContestantProfileRepository profileRepository;
    private final ContestRankingRepository rankingRepository;
    private final ProgrammingContestRepository contestRepository;
    private final ChallengeService challengeService;

    public SubmissionService(ChallengeSubmissionRepository submissionRepository,
                             ContestantProfileRepository profileRepository,
                             ContestRankingRepository rankingRepository,
                             ProgrammingContestRepository contestRepository,
                             ChallengeService challengeService) {
        this.submissionRepository = submissionRepository;
        this.profileRepository = profileRepository;
        this.rankingRepository = rankingRepository;
        this.contestRepository = contestRepository;
        this.challengeService = challengeService;
    }

    public ChallengeSubmission processSubmission(Long challengeId, Long contestId, String sourceCode, String username) {
        ContestantProfile contestant = profileRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Contestant profile not found for user: " + username));

        CodingChallenge challenge = challengeService.getChallengeById(challengeId);

        ChallengeSubmission submission = new ChallengeSubmission();
        submission.setSourceCode(sourceCode);
        submission.setChallenge(challenge);
        submission.setContestant(contestant);
        submission.setVerdict(simulateVerdict());

        if (contestId != null) {
            ProgrammingContest contest = contestRepository.findById(contestId)
                    .orElse(null);
            submission.setContest(contest);
        }

        if (submission.getVerdict() == Verdict.ACCEPTED) {
            int points = calculatePoints(challenge);
            submission.setPointsEarned(points);
            submission.setExecutionTimeMs(new Random().nextInt(challenge.getTimeLimitMs()));

            contestant.setTotalPoints(contestant.getTotalPoints() + points);
            profileRepository.save(contestant);

            if (contestId != null) {
                ContestRanking ranking = rankingRepository
                        .findByContestIdAndContestantId(contestId, contestant.getId())
                        .orElse(null);
                if (ranking != null) {
                    ranking.setCurrentScore(ranking.getCurrentScore() + points);
                    ranking.setPenaltyTime(ranking.getPenaltyTime() + submission.getExecutionTimeMs());
                    rankingRepository.save(ranking);
                }
            }
        } else {
            submission.setPointsEarned(0);
            submission.setExecutionTimeMs(0);
        }

        return submissionRepository.save(submission);
    }

    public List<ChallengeSubmission> getSubmissionsByContestant(Long contestantId) {
        return submissionRepository.findByContestantId(contestantId);
    }

    public List<ChallengeSubmission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public void deleteSubmission(Long id) {
        ChallengeSubmission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with ID: " + id));
        submissionRepository.delete(submission);
    }

    private Verdict simulateVerdict() {
        Verdict[] verdicts = Verdict.values();
        return verdicts[new Random().nextInt(verdicts.length)];
    }

    private int calculatePoints(CodingChallenge challenge) {
        return switch (challenge.getDifficulty()) {
            case EASY -> challenge.getBasePoints();
            case MEDIUM -> challenge.getBasePoints() * 2;
            case HARD -> challenge.getBasePoints() * 3;
        };
    }
}
