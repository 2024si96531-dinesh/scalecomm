package com.healthcare.healthrecordservice.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.healthrecordservice.domain.HealthRecord;
import com.healthcare.healthrecordservice.domain.HealthRecordRepository;

@Service
public class HealthRecordService {

    private final HealthRecordRepository repository;

    public HealthRecordService(HealthRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public HealthRecord create(HealthRecord record) { return repository.save(record); }

    @Transactional(readOnly = true)
    public HealthRecord get(String recordId) { return repository.findById(recordId).orElseThrow(() -> new RuntimeException("Health record not found: " + recordId)); }

    @Transactional(readOnly = true)
    public List<HealthRecord> search(String patientId) {
        if (patientId == null || patientId.isBlank()) {
            return repository.findAll();
        }
        return repository.findByPatientId(patientId);
    }
}