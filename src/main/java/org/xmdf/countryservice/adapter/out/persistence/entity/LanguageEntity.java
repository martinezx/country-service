package org.xmdf.countryservice.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "language")
@Getter
@Setter
@NoArgsConstructor
public class LanguageEntity extends BaseEntity {

  @Column(name = "iso639_2", nullable = false)
  private String iso6392;

  @Column(name = "iso639_1")
  private String iso6391;

  @Column(nullable = false)
  private String name;
}
