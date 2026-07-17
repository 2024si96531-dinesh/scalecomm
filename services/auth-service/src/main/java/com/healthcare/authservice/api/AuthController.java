package com.healthcare.authservice.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.authservice.application.AuthTokenService;
import com.healthcare.authservice.application.UserAccountService;
import com.healthcare.authservice.domain.UserAccount;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserAccountService userAccountService;
    private final AuthTokenService authTokenService;

    public AuthController(UserAccountService userAccountService, AuthTokenService authTokenService) {
        this.userAccountService = userAccountService;
        this.authTokenService = authTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            UserAccount account = userAccountService.authenticate(request.username(), request.password());
            AuthTokenService.IssuedToken issuedToken = authTokenService.issue(account);
            return ResponseEntity.ok(new LoginResponse(
                    issuedToken.accessToken(),
                    "Bearer",
                    issuedToken.expiresInSeconds(),
                    request.username(),
                    issuedToken.roles()));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record LoginResponse(String accessToken, String tokenType, long expiresInSeconds, String username,
                                List<String> roles) {
    }
}
