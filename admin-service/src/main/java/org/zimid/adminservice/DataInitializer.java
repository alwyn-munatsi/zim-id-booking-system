package org.zimid.adminservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.zimid.adminservice.model.AdminUser;
import org.zimid.adminservice.repository.AdminUserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (adminUserRepository.count() == 0) {
            createDefaultAdminUsers();
        }
    }

    private void createDefaultAdminUsers() {
        log.info("Creating default admin users...");

        // Create Super Admin
        AdminUser superAdmin = AdminUser.builder()
                .username("superadmin")
                .email("superadmin@rg.gov.zw")
                .password(passwordEncoder.encode("Admin@123"))
                .fullName("Super Administrator")
                .role(AdminUser.AdminRole.SUPER_ADMIN)
                .active(true)
                .build();

        adminUserRepository.save(superAdmin);
        log.info("Super Admin created - username: superadmin, password: Admin@123");

        // Create Regular Admin
        AdminUser admin = AdminUser.builder()
                .username("admin")
                .email("admin@rg.gov.zw")
                .password(passwordEncoder.encode("Admin@123"))
                .fullName("System Administrator")
                .role(AdminUser.AdminRole.ADMIN)
                .active(true)
                .build();

        adminUserRepository.save(admin);
        log.info("Admin created - username: admin, password: Admin@123");

        // Create Operator
        AdminUser operator = AdminUser.builder()
                .username("operator")
                .email("operator@rg.gov.zw")
                .password(passwordEncoder.encode("Operator@123"))
                .fullName("System Operator")
                .role(AdminUser.AdminRole.OPERATOR)
                .active(true)
                .build();

        adminUserRepository.save(operator);
        log.info("Operator created - username: operator, password: Operator@123");

        // Create Viewer
        AdminUser viewer = AdminUser.builder()
                .username("viewer")
                .email("viewer@rg.gov.zw")
                .password(passwordEncoder.encode("Viewer@123"))
                .fullName("System Viewer")
                .role(AdminUser.AdminRole.VIEWER)
                .active(true)
                .build();

        adminUserRepository.save(viewer);
        log.info("Viewer created - username: viewer, password: Viewer@123");

        log.info("Default admin users created successfully!");
    }
}