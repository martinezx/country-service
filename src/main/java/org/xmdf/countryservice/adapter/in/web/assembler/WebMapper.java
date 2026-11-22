package org.xmdf.countryservice.adapter.in.web.assembler;

import org.mapstruct.Mapper;
import org.xmdf.countryservice.adapter.in.web.model.CountryResponse;
import org.xmdf.countryservice.adapter.in.web.model.CurrencyResponse;
import org.xmdf.countryservice.adapter.in.web.model.LanguageResponse;
import org.xmdf.countryservice.adapter.in.web.model.RegionResponse;
import org.xmdf.countryservice.adapter.in.web.model.TimezoneResponse;
import org.xmdf.countryservice.domain.Country;
import org.xmdf.countryservice.domain.Currency;
import org.xmdf.countryservice.domain.Language;
import org.xmdf.countryservice.domain.Region;
import org.xmdf.countryservice.domain.Timezone;

/** Maps domain objects to the representations returned by the API. */
@Mapper
interface WebMapper {

  /**
   * Maps a region to its representation.
   *
   * @param region the region
   * @return the representation
   */
  RegionResponse toResponse(Region region);

  /**
   * Maps a timezone to its representation.
   *
   * @param timezone the timezone
   * @return the representation
   */
  TimezoneResponse toResponse(Timezone timezone);

  /**
   * Maps a currency to its representation.
   *
   * @param currency the currency
   * @return the representation
   */
  CurrencyResponse toResponse(Currency currency);

  /**
   * Maps a language to its representation.
   *
   * @param language the language
   * @return the representation
   */
  LanguageResponse toResponse(Language language);

  /**
   * Maps a country to its representation, without its region, currencies and languages.
   *
   * @param country the country
   * @return the representation
   */
  CountryResponse toResponse(Country country);
}
