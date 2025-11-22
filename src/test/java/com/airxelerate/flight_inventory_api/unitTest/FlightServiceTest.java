package com.airxelerate.flight_inventory_api.unitTest;


import com.airxelerate.flight_inventory_api.entities.Flight;
import com.airxelerate.flight_inventory_api.repositories.FlightRepository;
import com.airxelerate.flight_inventory_api.services.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight flight1, flight2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        flight1 = new Flight(null, "AF", 1234, LocalDate.now().plusDays(1), "CDG", "JFK");
        flight2 = new Flight(null, "BA", 4321, LocalDate.now().plusDays(2), "LHR", "JFK");
    }

    @Test
    void testAddFlight() {
        when(flightRepository.save(flight1)).thenReturn(flight1);
        Flight saved = flightService.addFlight(flight1);

        assertNotNull(saved);
        assertEquals("AF", saved.getCarrierCode());
        verify(flightRepository, times(1)).save(flight1);
    }

    @Test
    void testGetFlightById_Success() {
        flight1.setId(1L);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight1));

        Flight found = flightService.getFlightById(1L);
        assertEquals(1L, found.getId());
        assertEquals("AF", found.getCarrierCode());
    }

    @Test
    void testGetFlightById_NotFound() {
        when(flightRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> flightService.getFlightById(999L));
    }

    @Test
    void testDeleteFlight() {
        flight1.setId(1L);
        when(flightRepository.existsById(1L)).thenReturn(true);
        doNothing().when(flightRepository).deleteById(1L);

        flightService.deleteFlight(1L);
        verify(flightRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteFlight_NotFound() {
        when(flightRepository.existsById(999L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> flightService.deleteFlight(999L));
    }

    @Test
    void testListFlights() {
        when(flightRepository.findAll()).thenReturn(Arrays.asList(flight1, flight2));

        List<Flight> flights = flightService.listFlights();
        assertEquals(2, flights.size());
    }
}
