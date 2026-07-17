package com.healthcare.authservice.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.authservice.domain.UserAccount;
import com.healthcare.authservice.domain.UserAccountRepository;

@Service
public class UserAccountService {

    private final UserAccountRepository repository;

    public UserAccountService(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserAccount create(UserAccount userAccount) { return repository.save(userAccount); }
    @Transactional(readOnly = true)
    public UserAccount get(String userId) { return repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId)); }
    @Transactional(readOnly = true)
    public List<UserAccount> search(String username) { return username == null || username.isBlank() ? repository.findAll() : repository.findByUsernameContainingIgnoreCase(username); }

    @Transactional(readOnly = true)
    public UserAccount authenticate(String username, String password) {
        UserAccount account = repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!account.getPasswordHash().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        return account;
    }
}