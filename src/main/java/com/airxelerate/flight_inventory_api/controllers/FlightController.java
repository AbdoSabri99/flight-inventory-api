package com.airxelerate.flight_inventory_api.controllers;

import com.airxelerate.flight_inventory_api.entities.Flight;
import com.airxelerate.flight_inventory_api.services.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public List<Flight> listFlights() {
        return flightService.listFlights();
    }

    @GetMapping("/{id}")
    public Flight getFlight(@PathVariable Long id) {
        return flightService.getFlightById(id);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Flight addFlight(@RequestBody @Valid Flight flight) {
        return flightService.addFlight(flight);
    }

    @PutMapping("/{id}")
    public Flight updateFlight(@PathVariable Long id, @RequestBody Flight flight) {
        return flightService.updateFlight(id, flight);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
    }
}
