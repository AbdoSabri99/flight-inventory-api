package com.airxelerate.flight_inventory_api.repositories;

import com.airxelerate.flight_inventory_api.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {}
