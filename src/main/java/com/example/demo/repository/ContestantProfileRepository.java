package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.ContestantProfile;

public interface ContestantProfileRepository extends JpaRepository<ContestantProfile, Long> {
    Optional<ContestantProfile> findByUsername(String username);
    Optional<ContestantProfile> findByUserId(Long userId);
}
