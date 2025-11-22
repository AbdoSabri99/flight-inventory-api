package com.airxelerate.flight_inventory_api.repositories;

import com.airxelerate.flight_inventory_api.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}

