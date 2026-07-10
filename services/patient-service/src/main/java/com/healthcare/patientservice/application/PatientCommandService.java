package com.healthcare.patientservice.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.patientservice.api.PatientRequest;
import com.healthcare.patientservice.api.PatientResponse;
import com.healthcare.patientservice.domain.Patient;
import com.healthcare.patientservice.domain.PatientRepository;
import com.healthcare.patientservice.messaging.PatientEventPublisher;

@Service
public class PatientCommandService {

    private final PatientRepository patientRepository;
    private final PatientEventPublisher patientEventPublisher;

    public PatientCommandService(PatientRepository patientRepository, PatientEventPublisher patientEventPublisher) {
        this.patientRepository = patientRepository;
        this.patientEventPublisher = patientEventPublisher;
    }

    @Transactional
    public PatientResponse create(PatientRequest request) {
        Patient patient = patientRepository.save(PatientMapper.newEntity(request));
        patientEventPublisher.publishCreated(patient);
        return PatientMapper.toResponse(patient);
    }

    @Transactional
    public PatientResponse update(String patientId, PatientRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
        PatientMapper.apply(request, patient);
        Patient saved = patientRepository.save(patient);
        patientEventPublisher.publishUpdated(saved);
        return PatientMapper.toResponse(saved);
    }
}