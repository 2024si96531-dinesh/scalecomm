package com.healthcare.insuranceservice.api;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.insuranceservice.application.PolicyService;
import com.healthcare.insuranceservice.domain.Policy;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/policies")
@Tag(name = "Policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<PolicyResponse> create(@Valid @RequestBody PolicyRequest request) {
        Policy policy = new Policy();
        policy.setPatientId(request.patientId());
        policy.setProviderName(request.providerName());
        policy.setPolicyNumber(request.policyNumber());
        policy.setStatus(request.status());
        policy.setEffectiveFrom(request.effectiveFrom());
        policy.setEffectiveTo(request.effectiveTo());
        Policy saved = policyService.create(policy);
        return ResponseEntity.created(URI.create("/api/v1/policies/" + saved.getPolicyId())).body(PolicyResponse.from(saved));
    }

    @GetMapping("/{policyId}")
    public PolicyResponse get(@PathVariable String policyId) {
        return PolicyResponse.from(policyService.get(policyId));
    }

    @GetMapping
    public List<PolicyResponse> search(@RequestParam(required = false) String patientId) {
        return policyService.search(patientId).stream().map(PolicyResponse::from).toList();
    }

    public record PolicyRequest(@NotBlank String patientId, @NotBlank String providerName, @NotBlank String policyNumber, @NotBlank String status, LocalDate effectiveFrom, LocalDate effectiveTo) {
    }

    public record PolicyResponse(String policyId, String patientId, String providerName, String policyNumber, String status, LocalDate effectiveFrom, LocalDate effectiveTo) {
        static PolicyResponse from(Policy policy) { return new PolicyResponse(policy.getPolicyId(), policy.getPatientId(), policy.getProviderName(), policy.getPolicyNumber(), policy.getStatus(), policy.getEffectiveFrom(), policy.getEffectiveTo()); }
    }
}