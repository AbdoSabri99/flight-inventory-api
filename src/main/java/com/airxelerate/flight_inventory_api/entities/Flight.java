package com.airxelerate.flight_inventory_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Carrier code is required")
    @Pattern(regexp = "^[A-Z0-9]{2}$", message = "Carrier code must be exactly 2 uppercase letters or digits")
    private String carrierCode;

    // 4-digit flight number (1000-9999)
    @NotNull(message = "Flight number is required")
    @Min(value = 1000, message = "Flight number must be 4 digits")
    @Max(value = 9999, message = "Flight number must be 4 digits")
    private Integer flightNumber;

    // Flight date cannot be in the past
    @NotNull(message = "Flight date is required")
    @FutureOrPresent(message = "Flight date cannot be in the past")
    private LocalDate flightDate;

    // Origin airport code: exactly 3 uppercase letters
    @NotBlank(message = "Origin airport code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Origin must be exactly 3 uppercase letters")
    private String origin;

    // Destination airport code: exactly 3 uppercase letters
    @NotBlank(message = "Destination airport code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Destination must be exactly 3 uppercase letters")
    private String destination;

    // Optional: prevent origin and destination being the same
    @AssertTrue(message = "Origin and destination cannot be the same")
    public boolean isOriginDestinationDifferent() {
        if (origin == null || destination == null) return true; // skip validation if null
        return !origin.equals(destination);
    }
}
