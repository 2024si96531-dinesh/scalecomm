package com.healthcare.insuranceservice.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, String> {

    List<Policy> findByPatientId(String patientId);
}