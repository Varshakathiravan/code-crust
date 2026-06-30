package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "challenge_submissions")
public class ChallengeSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Verdict verdict;

    private Integer executionTimeMs;

    @Column(nullable = false)
    private Integer pointsEarned = 0;

    @ManyToOne
    @JoinColumn(name = "contestant_id", nullable = false)
    private ContestantProfile contestant;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private CodingChallenge challenge;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private ProgrammingContest contest;

    @Column(nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    public enum Verdict {
        ACCEPTED, WRONG_ANSWER, TIME_LIMIT_EXCEEDED, COMPILATION_ERROR, RUNTIME_ERROR
    }

    public ChallengeSubmission() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public Verdict getVerdict() {
        return verdict;
    }

    public void setVerdict(Verdict verdict) {
        this.verdict = verdict;
    }

    public Integer getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(Integer executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public Integer getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Integer pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public ContestantProfile getContestant() {
        return contestant;
    }

    public void setContestant(ContestantProfile contestant) {
        this.contestant = contestant;
    }

    public CodingChallenge getChallenge() {
        return challenge;
    }

    public void setChallenge(CodingChallenge challenge) {
        this.challenge = challenge;
    }

    public ProgrammingContest getContest() {
        return contest;
    }

    public void setContest(ProgrammingContest contest) {
        this.contest = contest;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
