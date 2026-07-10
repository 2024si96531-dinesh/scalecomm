package com.healthcare.patientservice.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.patientservice.application.PatientCommandService;
import com.healthcare.patientservice.application.PatientQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Patients")
public class PatientController {

    private final PatientCommandService patientCommandService;
    private final PatientQueryService patientQueryService;

    public PatientController(PatientCommandService patientCommandService, PatientQueryService patientQueryService) {
        this.patientCommandService = patientCommandService;
        this.patientQueryService = patientQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a patient")
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody PatientRequest request) {
        PatientResponse response = patientCommandService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/patients/" + response.patientId())).body(response);
    }

    @PutMapping("/{patientId}")
    @Operation(summary = "Update a patient")
    public PatientResponse update(@PathVariable String patientId, @Valid @RequestBody PatientRequest request) {
        return patientCommandService.update(patientId, request);
    }

    @GetMapping("/{patientId}")
    @Operation(summary = "Get a patient by id")
    public PatientResponse getById(@PathVariable String patientId) {
        return patientQueryService.getById(patientId);
    }

    @GetMapping
    @Operation(summary = "Search patients")
    public List<PatientResponse> search(@RequestParam(required = false) String lastName) {
        return patientQueryService.search(lastName);
    }
}