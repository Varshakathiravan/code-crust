package com.example.demo.service;

import com.example.demo.entity.ContestantProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ContestantProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final ContestantProfileRepository profileRepository;

    public ProfileService(ContestantProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<ContestantProfile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public ContestantProfile getProfileByUsername(String username) {
        return profileRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for username: " + username));
    }

    public ContestantProfile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user ID: " + userId));
    }

    public void updateProfile(Long id, ContestantProfile details) {
        ContestantProfile existing = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + id));
        existing.setBio(details.getBio());
        existing.setUsername(details.getUsername());
        profileRepository.save(existing);
    }

    public void deleteProfile(Long id) {
        ContestantProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID: " + id));
        profileRepository.delete(profile);
    }
}
