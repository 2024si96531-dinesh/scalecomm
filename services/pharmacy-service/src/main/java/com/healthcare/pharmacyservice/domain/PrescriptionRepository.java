package com.healthcare.pharmacyservice.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, String> {

    List<Prescription> findByPatientId(String patientId);
}