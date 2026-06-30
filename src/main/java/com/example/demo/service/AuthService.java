package com.example.demo.service;

import com.example.demo.entity.ContestantProfile;
import com.example.demo.entity.SystemUser;
import com.example.demo.entity.SystemUser.UserRole;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.ContestantProfileRepository;
import com.example.demo.repository.SystemUserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final SystemUserRepo userRepository;
    private final ContestantProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(SystemUserRepo userRepository,
                       ContestantProfileRepository profileRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(SystemUser user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username '" + user.getUsername() + "' already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.CONTESTANT);
        SystemUser savedUser = userRepository.save(user);

        ContestantProfile profile = new ContestantProfile();
        profile.setUsername(savedUser.getUsername());
        profile.setBio(user.getEmail() != null ? user.getEmail() : "");
        profile.setUser(savedUser);
        profileRepository.save(profile);
    }

    public String login(SystemUser loginUser) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword())
        );
        SystemUser dbUser = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + loginUser.getUsername()));
        return jwtService.generateToken(dbUser);
    }
}
