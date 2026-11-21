package org.xmdf.countryservice.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", insertable = false)
  private Instant updatedAt;

  @PreUpdate
  void onUpdate() {
    updatedAt = Instant.now();
  }
}
