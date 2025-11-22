package com.airxelerate.flight_inventory_api.util;

import com.airxelerate.flight_inventory_api.entities.AppUser;
import com.airxelerate.flight_inventory_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(userRepo.count() == 0) {
            userRepo.save(new AppUser(null, "admin", passwordEncoder.encode("admin123"), "ROLE_ADMIN"));
            userRepo.save(new AppUser(null, "user", passwordEncoder.encode("user123"), "ROLE_USER"));
        }
    }
}
