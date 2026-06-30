package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.ContestRanking;

public interface ContestRankingRepository extends JpaRepository<ContestRanking, Long> {
    Optional<ContestRanking> findByContestIdAndContestantId(Long contestId, Long contestantId);
    boolean existsByContestIdAndContestantId(Long contestId, Long contestantId);
}
