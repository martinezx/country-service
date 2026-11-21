package org.xmdf.countryservice.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "country")
@Getter
@Setter
@NoArgsConstructor
public class CountryEntity extends BaseEntity {

  @Column(name = "alpha2_code", nullable = false)
  private String alpha2Code;

  @Column(name = "alpha3_code", nullable = false)
  private String alpha3Code;

  @Column(name = "numeric_code", nullable = false)
  private String numericCode;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "region_id", nullable = false)
  private RegionEntity region;

  private Double latitude;

  private Double longitude;

  @ManyToMany
  @JoinTable(
      name = "country_currency",
      joinColumns = @JoinColumn(name = "country_id"),
      inverseJoinColumns = @JoinColumn(name = "currency_id"))
  private Set<CurrencyEntity> currencies = new LinkedHashSet<>();

  @ManyToMany
  @JoinTable(
      name = "country_language",
      joinColumns = @JoinColumn(name = "country_id"),
      inverseJoinColumns = @JoinColumn(name = "language_id"))
  private Set<LanguageEntity> languages = new LinkedHashSet<>();
}
