package com.healthcare.authservice.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.authservice.application.UserAccountService;
import com.healthcare.authservice.domain.UserAccount;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping
    public ResponseEntity<UserAccountResponse> create(@Valid @RequestBody UserAccountRequest request) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(request.username());
        userAccount.setPasswordHash(request.passwordHash());
        userAccount.setStatus(request.status());
        UserAccount saved = userAccountService.create(userAccount);
        return ResponseEntity.created(URI.create("/api/v1/users/" + saved.getUserId())).body(UserAccountResponse.from(saved));
    }

    @GetMapping("/{userId}")
    public UserAccountResponse get(@PathVariable String userId) {
        return UserAccountResponse.from(userAccountService.get(userId));
    }

    @GetMapping
    public List<UserAccountResponse> search(@RequestParam(required = false) String username) {
        return userAccountService.search(username).stream().map(UserAccountResponse::from).toList();
    }

    public record UserAccountRequest(@NotBlank String username, @NotBlank String passwordHash, @NotBlank String status) {
    }

    public record UserAccountResponse(String userId, String username, String status) {
        static UserAccountResponse from(UserAccount account) { return new UserAccountResponse(account.getUserId(), account.getUsername(), account.getStatus()); }
    }
}