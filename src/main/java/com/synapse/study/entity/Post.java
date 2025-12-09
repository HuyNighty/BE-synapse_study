package com.synapse.study.entity;

import com.synapse.study.enums.PostStatus;
import com.synapse.study.enums.PostType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE posts SET is_deleted = true, deleted_at = NOW() WHERE id = ? AND version = ?")
@SQLRestriction("is_deleted = false")
public class Post extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "slug", nullable = false, unique = true)
    String slug;

    @Column(name = "summary", length = 500)
    String summary;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    PostStatus status = PostStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    @Builder.Default
    PostType postType = PostType.CONCEPT;

    @Column(name = "view_count")
    @Builder.Default
    Long viewCount = 0L;

    @Column(name = "reading_time")
    Integer readingTime;

    @Column(name = "published_at")
    LocalDateTime publishedAt;

    @Version
    Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_id")
    Asset thumbnail;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PostTag> tags;
}