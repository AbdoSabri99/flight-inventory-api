package com.airxelerate.flight_inventory_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    // Accepts: ROLE_ADMIN or ROLE_ADMIN,ROLE_USER
    @NotBlank(message = "Roles cannot be empty")
    @Pattern(
            regexp = "(ROLE_[A-Z]+)(,ROLE_[A-Z]+)*",
            message = "Roles must be comma-separated and start with ROLE_ (e.g., ROLE_ADMIN,ROLE_USER)"
    )
    private String roles;

}
