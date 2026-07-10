package com.healthcare.patientservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        "spring.datasource.url=jdbc:h2:mem:patientdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "app.messaging.enabled=false"
})
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsAndReadsPatient() throws Exception {
        String responseBody = mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Ana",
                                  "lastName": "Miles",
                                  "dateOfBirth": "1992-08-17",
                                  "gender": "FEMALE",
                                  "phone": "+919999999999",
                                  "email": "ana.miles@example.com",
                                  "status": "ACTIVE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String patientId = responseBody.replaceAll(".*\"patientId\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(get("/api/v1/patients/{patientId}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Miles"));
    }
}