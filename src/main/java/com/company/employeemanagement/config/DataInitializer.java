package com.company.employeemanagement.config;

import com.company.employeemanagement.entity.Role;
import com.company.employeemanagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles() {
        return args -> {

            if (roleRepository.findByName("USER").isEmpty()) {
                Role user = new Role();
                user.setName("USER");
                roleRepository.save(user);
            }

            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role admin = new Role();
                admin.setName("ADMIN");
                roleRepository.save(admin);
            }
        };
    }
}