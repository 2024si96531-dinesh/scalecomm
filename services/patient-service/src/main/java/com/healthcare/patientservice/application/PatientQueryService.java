package com.healthcare.patientservice.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.patientservice.api.PatientResponse;
import com.healthcare.patientservice.domain.PatientRepository;

@Service
public class PatientQueryService {

    private final PatientRepository patientRepository;

    public PatientQueryService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Transactional(readOnly = true)
    public PatientResponse getById(String patientId) {
        return patientRepository.findById(patientId)
                .map(PatientMapper::toResponse)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> search(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            return patientRepository.findAll().stream().map(PatientMapper::toResponse).toList();
        }
        return patientRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(PatientMapper::toResponse)
                .toList();
    }
}