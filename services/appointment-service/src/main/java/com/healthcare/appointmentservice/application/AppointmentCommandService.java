package com.healthcare.appointmentservice.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.appointmentservice.api.AppointmentRequest;
import com.healthcare.appointmentservice.api.AppointmentResponse;
import com.healthcare.appointmentservice.domain.Appointment;
import com.healthcare.appointmentservice.domain.AppointmentRepository;
import com.healthcare.appointmentservice.messaging.AppointmentEventPublisher;

@Service
public class AppointmentCommandService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentEventPublisher appointmentEventPublisher;

    public AppointmentCommandService(AppointmentRepository appointmentRepository, AppointmentEventPublisher appointmentEventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentEventPublisher = appointmentEventPublisher;
    }

    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {
        Appointment appointment = appointmentRepository.save(AppointmentMapper.newEntity(request));
        appointmentEventPublisher.publishBooked(appointment);
        return AppointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse update(String appointmentId, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));
        AppointmentMapper.apply(request, appointment);
        Appointment saved = appointmentRepository.save(appointment);
        appointmentEventPublisher.publishUpdated(saved);
        return AppointmentMapper.toResponse(saved);
    }
}