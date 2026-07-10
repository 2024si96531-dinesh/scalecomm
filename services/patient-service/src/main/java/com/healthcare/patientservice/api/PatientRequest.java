package com.healthcare.patientservice.api;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PatientRequest(
        @Size(max = 64) String externalReference,
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        LocalDate dateOfBirth,
        @Size(max = 20) String gender,
        @Size(max = 32) String phone,
        @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 32) String status
) {
}