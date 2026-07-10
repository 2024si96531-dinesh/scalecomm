package com.healthcare.billingservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:billingdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "app.messaging.enabled=false",
        "spring.rabbitmq.listener.simple.auto-startup=false"
})
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsInvoice() throws Exception {
        mockMvc.perform(post("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "patientId": "patient-1",
                                  "appointmentId": "appointment-1",
                                  "amount": 550.00,
                                  "currency": "INR",
                                  "status": "ISSUED",
                                  "issuedAt": "2099-08-17T10:15:00",
                                  "dueAt": "2099-08-24T10:15:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invoiceId").isNotEmpty())
                .andExpect(jsonPath("$.currency").value("INR"));
    }
}