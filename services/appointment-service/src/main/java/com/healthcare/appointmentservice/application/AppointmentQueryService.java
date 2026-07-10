package com.healthcare.appointmentservice.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.appointmentservice.api.AppointmentResponse;
import com.healthcare.appointmentservice.domain.AppointmentRepository;

@Service
public class AppointmentQueryService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentQueryService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getById(String appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .map(AppointmentMapper::toResponse)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> search(String patientId) {
        if (patientId == null || patientId.isBlank()) {
            return appointmentRepository.findAll().stream().map(AppointmentMapper::toResponse).toList();
        }
        return appointmentRepository.findByPatientId(patientId).stream().map(AppointmentMapper::toResponse).toList();
    }
}