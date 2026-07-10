package com.healthcare.pharmacyservice.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.pharmacyservice.domain.Prescription;
import com.healthcare.pharmacyservice.domain.PrescriptionRepository;

@Service
public class PrescriptionService {

    private final PrescriptionRepository repository;

    public PrescriptionService(PrescriptionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Prescription create(Prescription prescription) { return repository.save(prescription); }
    @Transactional(readOnly = true)
    public Prescription get(String prescriptionId) { return repository.findById(prescriptionId).orElseThrow(() -> new RuntimeException("Prescription not found: " + prescriptionId)); }
    @Transactional(readOnly = true)
    public List<Prescription> search(String patientId) { return patientId == null || patientId.isBlank() ? repository.findAll() : repository.findByPatientId(patientId); }
}