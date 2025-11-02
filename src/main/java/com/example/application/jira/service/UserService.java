package com.example.application.jira.service;

import com.example.application.jira.model.User;
import com.example.application.jira.repository.RoleRepository;
import com.example.application.jira.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public List<User> getAllActiveUsers() {
        return userRepository.findAllActive();
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Transactional
    public User createUser(String username, String fullName, String email, String password, String avatarUrl, Set<String> roleNames) {
        User user = User.builder()
                .username(username)
                .fullName(fullName)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .avatarUrl(avatarUrl)
                .build();
        
        roleNames.forEach(roleName -> {
            roleRepository.findByName(roleName).ifPresent(user.getRoles()::add);
        });
        
        return userRepository.save(user);
    }
    
    @Transactional
    public void softDeleteUser(Long userId) {
        userRepository.findById(userId).ifPresent(User::softDelete);
    }
}

