package com.synapse.study.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Set;
import java.util.UUID;

@Builder
@Table(name = "users")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET is_deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("is_deleted = false")
public class User extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    UUID id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    String username;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    String email;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "first_name", length = 50)
    String firstName;

    @Column(name = "last_name", length = 50)
    String lastName;

    @Column(name = "bio", columnDefinition = "TEXT")
    String bio;

    @Column(name = "is_active")
    @Builder.Default
    Boolean isActive = true;

    @Version
    Long version;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    Asset avatar;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<UserRole> userRoles;
}
