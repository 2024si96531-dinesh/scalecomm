package com.healthcare.patientservice.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String> {

    List<Patient> findByLastNameContainingIgnoreCase(String lastName);
}