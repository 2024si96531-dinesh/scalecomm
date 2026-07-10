package com.healthcare.billingservice.application;

import com.healthcare.billingservice.api.InvoiceRequest;
import com.healthcare.billingservice.api.InvoiceResponse;
import com.healthcare.billingservice.domain.Invoice;

public final class InvoiceMapper {

    private InvoiceMapper() {
    }

    public static Invoice newEntity(InvoiceRequest request) {
        Invoice invoice = new Invoice();
        apply(request, invoice);
        return invoice;
    }

    public static void apply(InvoiceRequest request, Invoice invoice) {
        invoice.setPatientId(request.patientId());
        invoice.setAppointmentId(request.appointmentId());
        invoice.setAmount(request.amount());
        invoice.setCurrency(request.currency());
        invoice.setStatus(request.status());
        invoice.setIssuedAt(request.issuedAt());
        invoice.setDueAt(request.dueAt());
    }

    public static InvoiceResponse toResponse(Invoice invoice) {
        return new InvoiceResponse(invoice.getInvoiceId(), invoice.getPatientId(), invoice.getAppointmentId(), invoice.getAmount(), invoice.getCurrency(), invoice.getStatus(), invoice.getIssuedAt(), invoice.getDueAt(), invoice.getCreatedAt(), invoice.getUpdatedAt());
    }
}