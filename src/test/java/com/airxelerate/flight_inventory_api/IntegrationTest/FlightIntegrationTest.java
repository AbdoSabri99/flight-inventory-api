package com.airxelerate.flight_inventory_api.IntegrationTest;

import com.airxelerate.flight_inventory_api.config.SecurityConfig;
import com.airxelerate.flight_inventory_api.entities.Flight;
import com.airxelerate.flight_inventory_api.repositories.FlightRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({com.fasterxml.jackson.databind.ObjectMapper.class})

class FlightIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;


    private String tokenAdmin;
    private String tokenUser;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        flightRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        // Login as admin
        String adminJson = "{\"username\":\"admin\",\"password\":\"admin123\"}";
        tokenAdmin = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        tokenAdmin = objectMapper.readTree(tokenAdmin).get("token").asText();

        // Login as normal user
        String userJson = "{\"username\":\"user\",\"password\":\"user123\"}";
        tokenUser = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        tokenUser = objectMapper.readTree(tokenUser).get("token").asText();
    }

    @Test
    void testAddFlight_AdminSuccess() throws Exception {
        Flight flight = new Flight(null, "AF", 1234, LocalDate.now().plusDays(1), "CDG", "JFK");

        mockMvc.perform(post("/api/flights")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carrierCode").value("AF"))
                .andExpect(jsonPath("$.flightNumber").value(1234));
    }

    @Test
    void testAddFlight_UserForbidden() throws Exception {
        Flight flight = new Flight(null, "AF", 1234, LocalDate.now().plusDays(1), "CDG", "JFK");

        mockMvc.perform(post("/api/flights")
                        .header("Authorization", "Bearer " + tokenUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetFlight_Success() throws Exception {
        Flight saved = flightRepository.save(new Flight(null, "BA", 4321, LocalDate.now().plusDays(2), "LHR", "JFK"));

        mockMvc.perform(get("/api/flights/" + saved.getId())
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carrierCode").value("BA"));
    }

    @Test
    void testGetFlight_NotFound() throws Exception {
        mockMvc.perform(get("/api/flights/9999")
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"));
    }

    @Test
    void testListFlights() throws Exception {
        flightRepository.save(new Flight(null, "AF", 1234, LocalDate.now().plusDays(1), "CDG", "JFK"));
        flightRepository.save(new Flight(null, "BA", 4321, LocalDate.now().plusDays(2), "LHR", "JFK"));

        mockMvc.perform(get("/api/flights")
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testDeleteFlight_AdminSuccess() throws Exception {
        Flight saved = flightRepository.save(new Flight(null, "AF", 1234, LocalDate.now().plusDays(1), "CDG", "JFK"));

        mockMvc.perform(delete("/api/flights/" + saved.getId())
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteFlight_UserForbidden() throws Exception {
        Flight saved = flightRepository.save(new Flight(null, "AF", 1234, LocalDate.now().plusDays(1), "CDG", "JFK"));

        mockMvc.perform(delete("/api/flights/" + saved.getId())
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAddFlight_ValidationError() throws Exception {
        Flight invalid = new Flight(null, "A", 12, LocalDate.now().minusDays(1), "CD", "J");

        mockMvc.perform(post("/api/flights")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }
}
