package com.healthcare.pharmacyservice.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.pharmacyservice.application.PrescriptionService;
import com.healthcare.pharmacyservice.domain.Prescription;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/prescriptions")
@Tag(name = "Prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ResponseEntity<PrescriptionResponse> create(@Valid @RequestBody PrescriptionRequest request) {
        Prescription prescription = new Prescription();
        prescription.setPatientId(request.patientId());
        prescription.setRecordId(request.recordId());
        prescription.setMedicationName(request.medicationName());
        prescription.setDosage(request.dosage());
        prescription.setStatus(request.status());
        Prescription saved = prescriptionService.create(prescription);
        return ResponseEntity.created(URI.create("/api/v1/prescriptions/" + saved.getPrescriptionId())).body(PrescriptionResponse.from(saved));
    }

    @GetMapping("/{prescriptionId}")
    public PrescriptionResponse get(@PathVariable String prescriptionId) {
        return PrescriptionResponse.from(prescriptionService.get(prescriptionId));
    }

    @GetMapping
    public List<PrescriptionResponse> search(@RequestParam(required = false) String patientId) {
        return prescriptionService.search(patientId).stream().map(PrescriptionResponse::from).toList();
    }

    public record PrescriptionRequest(@NotBlank String patientId, String recordId, @NotBlank String medicationName, String dosage, @NotBlank String status) {
    }

    public record PrescriptionResponse(String prescriptionId, String patientId, String recordId, String medicationName, String dosage, String status) {
        static PrescriptionResponse from(Prescription prescription) { return new PrescriptionResponse(prescription.getPrescriptionId(), prescription.getPatientId(), prescription.getRecordId(), prescription.getMedicationName(), prescription.getDosage(), prescription.getStatus()); }
    }
}