package com.synapse.study.repository;

import com.synapse.study.entity.Asset;
import com.synapse.study.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {

    Optional<Post> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Post> findByThumbnail(Asset thumbnail);

    Page<Post> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}