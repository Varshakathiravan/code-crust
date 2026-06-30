package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contest_rankings")
public class ContestRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private ProgrammingContest contest;

    @ManyToOne
    @JoinColumn(name = "contestant_id", nullable = false)
    private ContestantProfile contestant;

    @Column(nullable = false)
    private Integer currentScore = 0;

    @Column(nullable = false)
    private Integer penaltyTime = 0;

    private Integer localRank;

    public ContestRanking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProgrammingContest getContest() {
        return contest;
    }

    public void setContest(ProgrammingContest contest) {
        this.contest = contest;
    }

    public ContestantProfile getContestant() {
        return contestant;
    }

    public void setContestant(ContestantProfile contestant) {
        this.contestant = contestant;
    }

    public Integer getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(Integer currentScore) {
        this.currentScore = currentScore;
    }

    public Integer getPenaltyTime() {
        return penaltyTime;
    }

    public void setPenaltyTime(Integer penaltyTime) {
        this.penaltyTime = penaltyTime;
    }

    public Integer getLocalRank() {
        return localRank;
    }

    public void setLocalRank(Integer localRank) {
        this.localRank = localRank;
    }
}
