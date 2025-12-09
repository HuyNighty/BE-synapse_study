package com.synapse.study.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseSoftDeleteEntity {

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @Column(name = "is_deleted")
    boolean deleted = false;

    public void undoDelete() {
        this.deleted = false;
        this.deletedAt = null;
    }
}
