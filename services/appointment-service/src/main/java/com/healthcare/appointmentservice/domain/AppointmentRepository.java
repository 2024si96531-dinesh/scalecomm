package com.healthcare.appointmentservice.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    List<Appointment> findByPatientId(String patientId);
}