package com.synapse.study.configuration;

import com.synapse.study.entity.Role;
import com.synapse.study.entity.User;
import com.synapse.study.entity.UserRole;
import com.synapse.study.repository.RoleRepository;
import com.synapse.study.repository.UserRepository;
import com.synapse.study.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository
    ) {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name("ADMIN")
                                    .description("System Administrator")
                                    .build()
                    ));

            Role userRoleDef = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name("USER")
                                    .description("Standard User")
                                    .build()
                    ));

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@synapse.com")
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .firstName("System")
                        .lastName("Admin")
                        .isActive(true)
                        .userRoles(new HashSet<>())
                        .build();

                userRepository.save(admin);

                UserRole userRole = UserRole.builder()
                        .id(new UserRole.UserRoleKey(admin.getId(), adminRole.getId()))
                        .user(admin)
                        .role(adminRole)
                        .build();

                userRoleRepository.save(userRole);
            }
        };
    }
}