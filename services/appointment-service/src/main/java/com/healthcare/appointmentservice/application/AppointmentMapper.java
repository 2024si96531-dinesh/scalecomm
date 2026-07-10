package com.healthcare.appointmentservice.application;

import com.healthcare.appointmentservice.api.AppointmentRequest;
import com.healthcare.appointmentservice.api.AppointmentResponse;
import com.healthcare.appointmentservice.domain.Appointment;

public final class AppointmentMapper {

    private AppointmentMapper() {
    }

    public static Appointment newEntity(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        apply(request, appointment);
        return appointment;
    }

    public static void apply(AppointmentRequest request, Appointment appointment) {
        appointment.setPatientId(request.patientId());
        appointment.setProviderReference(request.providerReference());
        appointment.setScheduledAt(request.scheduledAt());
        appointment.setStatus(request.status());
        appointment.setReason(request.reason());
    }

    public static AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getPatientId(),
                appointment.getProviderReference(),
                appointment.getScheduledAt(),
                appointment.getStatus(),
                appointment.getReason(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt()
        );
    }
}