package com.healthcare.appointmentservice.api;

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

import com.healthcare.appointmentservice.application.AppointmentCommandService;
import com.healthcare.appointmentservice.application.AppointmentQueryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/appointments")
@Tag(name = "Appointments")
public class AppointmentController {

    private final AppointmentCommandService appointmentCommandService;
    private final AppointmentQueryService appointmentQueryService;

    public AppointmentController(AppointmentCommandService appointmentCommandService, AppointmentQueryService appointmentQueryService) {
        this.appointmentCommandService = appointmentCommandService;
        this.appointmentQueryService = appointmentQueryService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentCommandService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/appointments/" + response.appointmentId())).body(response);
    }

    @PutMapping("/{appointmentId}")
    public AppointmentResponse update(@PathVariable String appointmentId, @Valid @RequestBody AppointmentRequest request) {
        return appointmentCommandService.update(appointmentId, request);
    }

    @GetMapping("/{appointmentId}")
    public AppointmentResponse get(@PathVariable String appointmentId) {
        return appointmentQueryService.getById(appointmentId);
    }

    @GetMapping
    public List<AppointmentResponse> search(@RequestParam(required = false) String patientId) {
        return appointmentQueryService.search(patientId);
    }
}