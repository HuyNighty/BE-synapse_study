package com.synapse.study.configuration;

import com.synapse.study.entity.*;
import com.synapse.study.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository,
            CategoryRepository categoryRepository
    ) {
        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").description("System Administrator").build()));

            Role userRoleDef = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("USER").description("Standard User").build()));

            Map<String, String> permissionsMap = new HashMap<>();

            permissionsMap.put("USER_READ", "Xem danh sách người dùng");
            permissionsMap.put("USER_DELETE", "Xóa người dùng");

            permissionsMap.put("CATEGORY_CREATE", "Tạo danh mục");
            permissionsMap.put("CATEGORY_DELETE", "Xóa danh mục");
            permissionsMap.put("POST_CREATE", "Đăng bài viết");
            permissionsMap.put("POST_DELETE", "Xóa bài viết");

            permissionsMap.put("COMMENT_CREATE", "Bình luận");
            permissionsMap.put("COMMENT_DELETE", "Xóa bình luận");

            permissionsMap.put("ROLE_CREATE", "Tạo vai trò mới");
            permissionsMap.put("PERMISSION_CREATE", "Tạo quyền mới");
            permissionsMap.put("ASSET_CREATE", "Upload file");

            List<Permission> allPermissions = new ArrayList<>();
            permissionsMap.forEach((name, desc) -> {
                Permission perm = permissionRepository.findByName(name)
                        .orElseGet(() -> permissionRepository.save(Permission.builder().name(name).description(desc).build()));
                allPermissions.add(perm);
            });


            assignPermissionsToRole(adminRole, allPermissions, rolePermissionRepository);

            List<String> userPermNames = List.of("POST_CREATE", "COMMENT_CREATE", "ASSET_CREATE");
            List<Permission> userPermissions = allPermissions.stream()
                    .filter(p -> userPermNames.contains(p.getName()))
                    .toList();
            assignPermissionsToRole(userRoleDef, userPermissions, rolePermissionRepository);

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

            if (categoryRepository.count() == 0) {
                categoryRepository.save(Category.builder().name("Java Spring Boot").slug("java-spring-boot").description("Backend").build());
                categoryRepository.save(Category.builder().name("ReactJS").slug("reactjs").description("Frontend").build());
            }
        };
    }

    private void assignPermissionsToRole(Role role, List<Permission> permissions, RolePermissionRepository repo) {
        for (Permission perm : permissions) {
            RolePermission.RolePermissionKey key = new RolePermission.RolePermissionKey(role.getId(), perm.getId());
            if (!repo.existsById(key)) {
                RolePermission rp = RolePermission.builder()
                        .id(key)
                        .role(role)
                        .permission(perm)
                        .build();
                repo.save(rp);
            }
        }
    }
}