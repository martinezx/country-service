package org.xmdf.countryservice.adapter.out.rest;

import java.util.List;
import org.springframework.stereotype.Component;
import org.xmdf.countryservice.application.port.out.CountriesSource;
import org.xmdf.countryservice.domain.Country;
import org.xmdf.countryservice.domain.Currency;
import org.xmdf.countryservice.domain.Language;
import org.xmdf.countryservice.domain.Region;
import org.xmdf.countryservice.domain.Timezone;

@Component
class CountriesRestAdapter implements CountriesSource {

  private final CountriesRestClient client;
  private final CountriesRestMapper mapper;

  CountriesRestAdapter(CountriesRestClient client, CountriesRestMapper mapper) {
    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public List<Region> fetchRegions() {
    return mapper.toRegions(client.getRegions());
  }

  @Override
  public List<Timezone> fetchTimezones() {
    return mapper.toTimezones(client.getTimezones());
  }

  @Override
  public List<Currency> fetchCurrencies(int limit, int offset) {
    return mapper.toCurrencies(client.getCurrencies(limit, offset));
  }

  @Override
  public List<Language> fetchLanguages(int limit, int offset) {
    return mapper.toLanguages(client.getLanguages(limit, offset));
  }

  @Override
  public List<Country> fetchCountries(int limit, int offset) {
    return mapper.toCountries(client.getCountries(limit, offset));
  }
}
