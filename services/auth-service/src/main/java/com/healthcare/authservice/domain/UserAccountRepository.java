package com.healthcare.authservice.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    List<UserAccount> findByUsernameContainingIgnoreCase(String username);

    Optional<UserAccount> findByUsernameIgnoreCase(String username);
}