package com.healthcare.healthrecordservice.api;

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

import com.healthcare.healthrecordservice.application.HealthRecordService;
import com.healthcare.healthrecordservice.domain.HealthRecord;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/records")
@Tag(name = "Health Records")
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    public HealthRecordController(HealthRecordService healthRecordService) {
        this.healthRecordService = healthRecordService;
    }

    @PostMapping
    public ResponseEntity<HealthRecordResponse> create(@Valid @RequestBody HealthRecordRequest request) {
        HealthRecord record = new HealthRecord();
        record.setPatientId(request.patientId());
        record.setEncounterReference(request.encounterReference());
        record.setRecordType(request.recordType());
        record.setSummary(request.summary());
        record.setStatus(request.status());
        HealthRecord saved = healthRecordService.create(record);
        return ResponseEntity.created(URI.create("/api/v1/records/" + saved.getRecordId())).body(HealthRecordResponse.from(saved));
    }

    @GetMapping("/{recordId}")
    public HealthRecordResponse get(@PathVariable String recordId) {
        return HealthRecordResponse.from(healthRecordService.get(recordId));
    }

    @GetMapping
    public List<HealthRecordResponse> search(@RequestParam(required = false) String patientId) {
        return healthRecordService.search(patientId).stream().map(HealthRecordResponse::from).toList();
    }

    public record HealthRecordRequest(@NotBlank String patientId, String encounterReference, @NotBlank String recordType, String summary, @NotBlank String status) {
    }

    public record HealthRecordResponse(String recordId, String patientId, String encounterReference, String recordType, String summary, String status) {
        static HealthRecordResponse from(HealthRecord record) {
            return new HealthRecordResponse(record.getRecordId(), record.getPatientId(), record.getEncounterReference(), record.getRecordType(), record.getSummary(), record.getStatus());
        }
    }
}