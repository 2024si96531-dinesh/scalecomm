package com.healthcare.authservice.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    List<UserAccount> findByUsernameContainingIgnoreCase(String username);
}