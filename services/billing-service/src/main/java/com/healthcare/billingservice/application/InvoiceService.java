package com.healthcare.billingservice.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.billingservice.api.InvoiceRequest;
import com.healthcare.billingservice.api.InvoiceResponse;
import com.healthcare.billingservice.domain.Invoice;
import com.healthcare.billingservice.domain.InvoiceRepository;
import com.healthcare.billingservice.messaging.InvoiceEventPublisher;
import com.healthcare.billingservice.messaging.dto.AppointmentBookedEvent;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceEventPublisher invoiceEventPublisher;

    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceEventPublisher invoiceEventPublisher) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceEventPublisher = invoiceEventPublisher;
    }

    @Transactional
    public InvoiceResponse create(InvoiceRequest request) {
        Invoice invoice = invoiceRepository.save(InvoiceMapper.newEntity(request));
        invoiceEventPublisher.publishIssued(invoice);
        return InvoiceMapper.toResponse(invoice);
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getById(String invoiceId) {
        return invoiceRepository.findById(invoiceId).map(InvoiceMapper::toResponse).orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> search(String patientId) {
        if (patientId == null || patientId.isBlank()) {
            return invoiceRepository.findAll().stream().map(InvoiceMapper::toResponse).toList();
        }
        return invoiceRepository.findByPatientId(patientId).stream().map(InvoiceMapper::toResponse).toList();
    }

    @Transactional
    public void createFromAppointment(AppointmentBookedEvent event) {
        Invoice invoice = new Invoice();
        invoice.setPatientId(event.patientId());
        invoice.setAppointmentId(event.appointmentId());
        invoice.setAmount(new java.math.BigDecimal("500.00"));
        invoice.setCurrency("INR");
        invoice.setStatus("ISSUED");
        invoice.setIssuedAt(LocalDateTime.now());
        invoice.setDueAt(LocalDateTime.now().plusDays(7));
        Invoice saved = invoiceRepository.save(invoice);
        invoiceEventPublisher.publishIssued(saved);
    }
}