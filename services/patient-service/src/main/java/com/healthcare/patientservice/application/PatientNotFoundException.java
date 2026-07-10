package com.healthcare.patientservice.application;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(String patientId) {
        super("Patient not found: " + patientId);
    }
}