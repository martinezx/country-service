package org.xmdf.countryservice.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "currency")
@Getter
@Setter
@NoArgsConstructor
public class CurrencyEntity extends BaseEntity {

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String name;

  private String symbol;
}
