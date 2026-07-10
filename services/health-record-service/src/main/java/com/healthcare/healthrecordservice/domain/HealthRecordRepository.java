package com.healthcare.healthrecordservice.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, String> {

    List<HealthRecord> findByPatientId(String patientId);
}