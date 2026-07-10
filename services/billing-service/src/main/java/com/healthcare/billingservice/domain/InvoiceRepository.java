package com.healthcare.billingservice.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    List<Invoice> findByPatientId(String patientId);
}