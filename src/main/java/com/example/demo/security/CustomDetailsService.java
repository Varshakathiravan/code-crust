package com.example.demo.security;
import java.util.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.SystemUser;
import com.example.demo.repository.SystemUserRepo;
@Service
public class CustomDetailsService implements UserDetailsService{
        private final SystemUserRepo userRepository ;

    public CustomDetailsService(SystemUserRepo userRepository) {
        this.userRepository = userRepository;
    }
    @Override
public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        SystemUser user = userRepository.findByUsername(username).orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())) );
    }

}
