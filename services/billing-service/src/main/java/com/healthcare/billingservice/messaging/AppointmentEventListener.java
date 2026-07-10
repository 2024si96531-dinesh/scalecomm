package com.healthcare.billingservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.healthcare.billingservice.application.InvoiceService;
import com.healthcare.billingservice.config.MessagingConfig;
import com.healthcare.billingservice.messaging.dto.AppointmentBookedEvent;

@Component
public class AppointmentEventListener {

    private final InvoiceService invoiceService;

    public AppointmentEventListener(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @RabbitListener(queues = MessagingConfig.APPOINTMENT_EVENTS_QUEUE)
    public void onAppointmentBooked(AppointmentBookedEvent event) {
        invoiceService.createFromAppointment(event);
    }
}