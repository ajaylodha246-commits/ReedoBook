
package com.reedobook.config;

import com.reedobook.entity.Role;
import com.reedobook.entity.User;
import com.reedobook.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            String email = "admin@reedobook.com";

            if (userRepository.findByEmail(email).isEmpty()) {
                User admin = new User();

                admin.setName("Reedobook Admin");
                admin.setEmail(email);
                admin.setPassword(
                        passwordEncoder.encode("Admin@12345"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println(
                        "Initial admin account created.");
            }
        };
    }
}
