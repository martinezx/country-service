package org.xmdf.countryservice.adapter.out.rest;

import java.util.List;
import java.util.Optional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.xmdf.countryservice.adapter.out.rest.dto.CountryRestDto;
import org.xmdf.countryservice.adapter.out.rest.dto.CurrencyRestDto;
import org.xmdf.countryservice.adapter.out.rest.dto.LanguageRestDto;
import org.xmdf.countryservice.adapter.out.rest.dto.TimezoneRestDto;
import org.xmdf.countryservice.adapter.out.rest.exception.CountriesApiRequestException;
import org.xmdf.countryservice.adapter.out.rest.exception.CountriesApiResponseException;
import org.xmdf.countryservice.adapter.out.rest.exception.CountriesApiUnavailableException;

@Component
public class CountriesRestClient {

  private static final int DEFAULT_LIMIT = 20;
  private static final int DEFAULT_OFFSET = 0;

  private final RestClient restClient;
  private final RetryTemplate retryTemplate;

  public CountriesRestClient(RestClient restClient, RetryTemplate retryTemplate) {
    this.restClient = restClient;
    this.retryTemplate = retryTemplate;
  }

  public List<String> getRegions() {
    return get("/regions", null, null, null, null, new ParameterizedTypeReference<>() {});
  }

  public List<TimezoneRestDto> getTimezones() {
    return get("/timezones", null, null, null, null, new ParameterizedTypeReference<>() {});
  }

  public List<LanguageRestDto> getLanguages(Integer limit, Integer offset) {
    return get(
        "/languages",
        "name,iso639_1,iso639_2",
        "iso639_1",
        limit,
        offset,
        new ParameterizedTypeReference<>() {});
  }

  public List<CurrencyRestDto> getCurrencies(Integer limit, Integer offset) {
    return get("/currencies", null, "code", limit, offset, new ParameterizedTypeReference<>() {});
  }

  public List<CountryRestDto> getCountries(Integer limit, Integer offset) {
    return get(
        "/countries",
        "region,name,latlng,numericCode,alpha2Code,alpha3Code,languages,currencies",
        "numericCode",
        limit,
        offset,
        new ParameterizedTypeReference<>() {});
  }

  private <T> List<T> get(
      String path,
      String fields,
      String sort,
      Integer limit,
      Integer offset,
      ParameterizedTypeReference<List<T>> type) {
    try {
      List<T> body =
          retryTemplate.execute(context ->
              restClient
                  .get()
                  .uri(uri -> {
                    uri.path(path).queryParamIfPresent("fields", Optional.ofNullable(fields));
                    if (sort != null) {
                      uri.queryParam("sort", sort).queryParam("order", "asc");
                    }
                    if (limit != null || offset != null) {
                      uri.queryParam("limit", limit != null ? limit : DEFAULT_LIMIT)
                          .queryParam("offset", offset != null ? offset : DEFAULT_OFFSET);
                    }
                    return uri.build();
                  })
                  .retrieve()
                  .body(type));
      return body != null ? body : List.of();
    } catch (HttpClientErrorException.TooManyRequests
        | HttpServerErrorException
        | ResourceAccessException e) {
      throw new CountriesApiUnavailableException(describe(path, limit, offset, e), e);
    } catch (RestClientResponseException e) {
      throw new CountriesApiRequestException(
          describe(path, limit, offset, e), e.getStatusCode(), e);
    } catch (RestClientException e) {
      throw new CountriesApiResponseException(describe(path, limit, offset, e), e);
    }
  }

  private static String describe(String path, Integer limit, Integer offset, Exception e) {
    return "Countries API call failed: GET %s limit=%s offset=%s (%s)"
        .formatted(path, limit, offset, e.getMessage());
  }
}
