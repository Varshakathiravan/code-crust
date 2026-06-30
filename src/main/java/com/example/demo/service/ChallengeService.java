package com.example.demo.service;

import com.example.demo.entity.CodingChallenge;
import com.example.demo.entity.SystemUser;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CodingChallengeRepository;
import com.example.demo.repository.SystemUserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChallengeService {

    private final CodingChallengeRepository challengeRepository;
    private final SystemUserRepo userRepository;

    public ChallengeService(CodingChallengeRepository challengeRepository,
                            SystemUserRepo userRepository) {
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }

    public List<CodingChallenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    public CodingChallenge getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Challenge not found with ID: " + id));
    }

    public void createChallenge(CodingChallenge challenge, String username) {
        SystemUser setter = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        challenge.setSetter(setter);
        challengeRepository.save(challenge);
    }

    public void updateChallenge(Long id, CodingChallenge details) {
        CodingChallenge existing = getChallengeById(id);
        existing.setTitle(details.getTitle());
        existing.setDescription(details.getDescription());
        existing.setDifficulty(details.getDifficulty());
        existing.setBasePoints(details.getBasePoints());
        existing.setTimeLimitMs(details.getTimeLimitMs());
        challengeRepository.save(existing);
    }

    public void deleteChallenge(Long id) {
        CodingChallenge challenge = getChallengeById(id);
        challengeRepository.delete(challenge);
    }
}
