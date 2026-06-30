package com.example.demo.service;

import com.example.demo.entity.ContestRanking;
import com.example.demo.entity.ContestantProfile;
import com.example.demo.entity.ProgrammingContest;
import com.example.demo.entity.ProgrammingContest.ContestStatus;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ContestRankingRepository;
import com.example.demo.repository.ContestantProfileRepository;
import com.example.demo.repository.ProgrammingContestRepository;
import com.example.demo.repository.SystemUserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestService {

    private final ProgrammingContestRepository contestRepository;
    private final ContestRankingRepository rankingRepository;
    private final ContestantProfileRepository profileRepository;
    private final SystemUserRepo userRepository;

    public ContestService(ProgrammingContestRepository contestRepository,
                          ContestRankingRepository rankingRepository,
                          ContestantProfileRepository profileRepository,
                          SystemUserRepo userRepository) {
        this.contestRepository = contestRepository;
        this.rankingRepository = rankingRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public List<ProgrammingContest> getAllContests() {
        return contestRepository.findAll();
    }

    public ProgrammingContest getContestById(Long id) {
        return contestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contest not found with ID: " + id));
    }

    public void createContest(ProgrammingContest contest) {
        if (contest.getStartTime().isAfter(contest.getEndTime())) {
            throw new BusinessValidationException("Start time must be before end time");
        }
        contestRepository.save(contest);
    }

    public void updateContest(Long id, ProgrammingContest details) {
        ProgrammingContest existing = getContestById(id);
        existing.setTitle(details.getTitle());
        existing.setStartTime(details.getStartTime());
        existing.setEndTime(details.getEndTime());
        existing.setCapacity(details.getCapacity());
        if (details.getStatus() != null) {
            existing.setStatus(details.getStatus());
        }
        contestRepository.save(existing);
    }

    public void deleteContest(Long id) {
        ProgrammingContest contest = getContestById(id);
        contestRepository.delete(contest);
    }

    public void enrollParticipant(Long contestId, String username) {
        ProgrammingContest contest = getContestById(contestId);
        if (contest.getStatus() != ContestStatus.UPCOMING) {
            throw new BusinessValidationException("Cannot enroll in a contest that is not UPCOMING");
        }

        ContestantProfile profile = profileRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Contestant profile not found for user: " + username));

        if (rankingRepository.existsByContestIdAndContestantId(contestId, profile.getId())) {
            throw new BusinessValidationException("Already enrolled in this contest");
        }

        long enrolledCount = rankingRepository.count();
        if (enrolledCount >= contest.getCapacity()) {
            throw new BusinessValidationException("Contest is at full capacity");
        }

        ContestRanking ranking = new ContestRanking();
        ranking.setContest(contest);
        ranking.setContestant(profile);
        ranking.setCurrentScore(0);
        ranking.setPenaltyTime(0);
        rankingRepository.save(ranking);
    }
}
