package com.playko.userservice.service.impl;

import com.playko.userservice.entities.Role;
import com.playko.userservice.entities.User;
import com.playko.userservice.repository.IRoleRepository;
import com.playko.userservice.repository.IUserRepository;
import com.playko.userservice.service.IAuthPasswordEncoderPort;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    private final IUserRepository userRepository;
    private final IAuthPasswordEncoderPort passwordEncoder;
    private final IRoleRepository roleRepository;

    public DatabaseInitializer(IUserRepository userRepository, IAuthPasswordEncoderPort passwordEncoder, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initialize() {
        initializeRoles();
        initializeAdminUser();
    }

    private void initializeRoles() {
        createRoleIfNotExists("ROLE_ADMIN", "ROLE_ADMIN");
        createRoleIfNotExists("ROLE_PARTNER", "ROLE_PARTNER");
    }

    private void createRoleIfNotExists(String name, String description) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }

    private void initializeAdminUser() {
        if (userRepository.findByEmail("admin@mail.com") == null) {
            User admin = new User();
            admin.setName("Admin");
            admin.setSurname("AdminSurname");
            admin.setPhoneNumber("+57 3136824595");
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encodePassword("admin"));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            admin.setRole(adminRole);

            userRepository.save(admin);
        }
    }
}
