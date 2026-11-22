package org.xmdf.countryservice.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "timezone")
@Getter
@Setter
@NoArgsConstructor
public class TimezoneEntity extends BaseEntity {

  @Column(nullable = false)
  private String timezone;

  @Column(nullable = false)
  private String utcOffset;

  @Column(nullable = false)
  private Integer utcOffsetMinutes;
}
