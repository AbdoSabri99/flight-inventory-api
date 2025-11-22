package com.airxelerate.flight_inventory_api.services;

import com.airxelerate.flight_inventory_api.entities.Flight;
import com.airxelerate.flight_inventory_api.repositories.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Flight with id " + id + " not found"));
    }

    public List<Flight> listFlights() {
        return flightRepository.findAll();
    }

    public void deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new NoSuchElementException("Flight with id " + id + " not found");
        }
        flightRepository.deleteById(id);
    }

    public Flight updateFlight(Long id, Flight updatedFlight) {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Flight with id " + id + " not found"));

        existingFlight.setCarrierCode(updatedFlight.getCarrierCode());
        existingFlight.setFlightNumber(updatedFlight.getFlightNumber());
        existingFlight.setFlightDate(updatedFlight.getFlightDate());
        existingFlight.setOrigin(updatedFlight.getOrigin());
        existingFlight.setDestination(updatedFlight.getDestination());

        return flightRepository.save(existingFlight);
    }
}
