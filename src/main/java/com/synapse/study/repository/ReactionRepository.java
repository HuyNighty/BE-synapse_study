package com.synapse.study.repository;

import com.synapse.study.entity.Reaction;
import com.synapse.study.enums.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    boolean existsByUserIdAndPostId(UUID userId, UUID postId);

    Optional<Reaction> findByUserIdAndPostId(UUID userId, UUID postId);

    long countByPostIdAndType(UUID postId, ReactionType type);

    long countByPostId(UUID postId);
}