package com.healthcare.patientservice.application;

import java.util.UUID;

import com.healthcare.patientservice.api.PatientRequest;
import com.healthcare.patientservice.api.PatientResponse;
import com.healthcare.patientservice.domain.Patient;

public final class PatientMapper {

    private PatientMapper() {
    }

    public static Patient newEntity(PatientRequest request) {
        Patient patient = new Patient();
        patient.setPatientId(UUID.randomUUID().toString());
        apply(request, patient);
        return patient;
    }

    public static void apply(PatientRequest request, Patient patient) {
        patient.setExternalReference(request.externalReference());
        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setDateOfBirth(request.dateOfBirth());
        patient.setGender(request.gender());
        patient.setPhone(request.phone());
        patient.setEmail(request.email());
        patient.setStatus(request.status());
    }

    public static PatientResponse toResponse(Patient patient) {
        return new PatientResponse(
                patient.getPatientId(),
                patient.getExternalReference(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getStatus(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}