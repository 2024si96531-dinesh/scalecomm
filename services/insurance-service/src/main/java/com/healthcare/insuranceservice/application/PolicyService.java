package com.healthcare.insuranceservice.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.insuranceservice.domain.Policy;
import com.healthcare.insuranceservice.domain.PolicyRepository;

@Service
public class PolicyService {

    private final PolicyRepository repository;

    public PolicyService(PolicyRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Policy create(Policy policy) { return repository.save(policy); }
    @Transactional(readOnly = true)
    public Policy get(String policyId) { return repository.findById(policyId).orElseThrow(() -> new RuntimeException("Policy not found: " + policyId)); }
    @Transactional(readOnly = true)
    public List<Policy> search(String patientId) { return patientId == null || patientId.isBlank() ? repository.findAll() : repository.findByPatientId(patientId); }
}