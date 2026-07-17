package com.healthcare.authservice.application;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.healthcare.authservice.domain.UserAccount;

@Service
public class AuthTokenService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expiresMinutes;
    private final Set<String> adminUsernames;
    private final Set<String> partnerUsernames;

    public AuthTokenService(
            JwtEncoder jwtEncoder,
            @Value("${app.security.jwt.issuer}") String issuer,
            @Value("${app.security.jwt.expires-minutes}") long expiresMinutes,
            @Value("${app.security.role-mapping.admin-usernames}") List<String> adminUsernames,
            @Value("${app.security.role-mapping.partner-usernames}") List<String> partnerUsernames) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expiresMinutes = expiresMinutes;
        this.adminUsernames = normalize(adminUsernames);
        this.partnerUsernames = normalize(partnerUsernames);
    }

    public IssuedToken issue(UserAccount account) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(expiresMinutes * 60);
        List<String> roles = resolveRoles(account.getUsername());

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(account.getUserId())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("username", account.getUsername())
                .claim("roles", roles)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(() -> "HS256").build(), claimsSet))
                .getTokenValue();

        return new IssuedToken(token, expiresAt.getEpochSecond() - issuedAt.getEpochSecond(), roles);
    }

    private List<String> resolveRoles(String username) {
        String normalized = username.toLowerCase();
        List<String> roles = new ArrayList<>();
        if (adminUsernames.contains(normalized)) {
            roles.add("ADMIN");
            roles.add("DOCTOR");
        } else if (partnerUsernames.contains(normalized)) {
            roles.add("PARTNER");
        } else {
            roles.add("PATIENT");
        }
        return roles;
    }

    private Set<String> normalize(List<String> usernames) {
        Set<String> normalized = new HashSet<>();
        for (String username : usernames) {
            String value = username == null ? "" : username.trim().toLowerCase();
            if (!value.isBlank()) {
                normalized.add(value);
            }
        }
        return normalized;
    }

    public record IssuedToken(String accessToken, long expiresInSeconds, List<String> roles) {
    }
}
