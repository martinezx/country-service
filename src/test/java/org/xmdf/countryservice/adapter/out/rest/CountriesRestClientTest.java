package org.xmdf.countryservice.adapter.out.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.xmdf.countryservice.adapter.out.rest.dto.CountryRestDto;
import org.xmdf.countryservice.adapter.out.rest.dto.CurrencyRestDto;
import org.xmdf.countryservice.adapter.out.rest.dto.LanguageRestDto;
import org.xmdf.countryservice.adapter.out.rest.exception.CountriesApiRequestException;
import org.xmdf.countryservice.adapter.out.rest.exception.CountriesApiResponseException;
import org.xmdf.countryservice.adapter.out.rest.exception.CountriesApiUnavailableException;

class CountriesRestClientTest {

  private static final String BASE_URL = "https://countries.test";

  private MockRestServiceServer server;
  private CountriesRestClient client;

  @BeforeEach
  void setUp() {
    RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
    server = MockRestServiceServer.bindTo(builder).build();
    client = new CountriesRestClient(builder.build(), RestConfig.countriesRetryTemplate(1));
  }

  @AfterEach
  void tearDown() {
    server.verify();
  }

  @Test
  void getRegions_sendsNoPagingParametersAndParsesResponse() {
    server
        .expect(requestTo(BASE_URL + "/regions"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("[\"Asia\",\"Europe\"]", MediaType.APPLICATION_JSON));

    assertThat(client.getRegions()).containsExactly("Asia", "Europe");
  }

  @Test
  void getLanguages_sendsGivenLimitAndOffsetAndParsesIsoCodes() {
    server
        .expect(queryParam("limit", "10"))
        .andExpect(queryParam("offset", "30"))
        .andExpect(queryParam("sort", "iso639_1"))
        .andExpect(queryParam("order", "asc"))
        .andRespond(
            withSuccess(
                "[{\"name\":\"Afar\",\"iso639_1\":\"aa\",\"iso639_2\":\"aar\"}]",
                MediaType.APPLICATION_JSON));

    List<LanguageRestDto> languages = client.getLanguages(10, 30);

    assertThat(languages).containsExactly(new LanguageRestDto("Afar", "aa", "aar"));
  }

  @Test
  void getCurrencies_appliesDefaultLimitWhenOnlyOffsetIsGiven() {
    server
        .expect(queryParam("limit", "20"))
        .andExpect(queryParam("offset", "5"))
        .andRespond(
            withSuccess(
                "[{\"code\":\"AFN\",\"name\":\"Afghan afghani\",\"symbol\":\"؋\"}]",
                MediaType.APPLICATION_JSON));

    List<CurrencyRestDto> currencies = client.getCurrencies(null, 5);

    assertThat(currencies).containsExactly(new CurrencyRestDto("AFN", "Afghan afghani", "؋"));
  }

  @Test
  void getCurrencies_appliesDefaultOffsetWhenOnlyLimitIsGiven() {
    server
        .expect(queryParam("limit", "7"))
        .andExpect(queryParam("offset", "0"))
        .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

    assertThat(client.getCurrencies(7, null)).isEmpty();
  }

  @Test
  void getCountries_parsesNestedLanguagesCurrenciesAndIntegerLatLng() {
    server
        .expect(queryParam("limit", "1"))
        .andExpect(queryParam("offset", "0"))
        .andRespond(
            withSuccess(
                """
                [{"region":"Asia","name":"Afghanistan","latlng":[33,65],"numericCode":"004",
                  "alpha2Code":"AF","alpha3Code":"AFG",
                  "languages":[{"name":"Pashto","iso639_1":"ps","iso639_2":"pus","nativeName":"پښتو"}],
                  "currencies":[{"code":"AFN","name":"Afghan afghani","symbol":"؋"}]}]
                """,
                MediaType.APPLICATION_JSON));

    List<CountryRestDto> countries = client.getCountries(1, 0);

    assertThat(countries)
        .containsExactly(
            new CountryRestDto(
                "Asia",
                "Afghanistan",
                List.of(33.0, 65.0),
                "004",
                "AF",
                "AFG",
                List.of(new LanguageRestDto("Pashto", "ps", "pus")),
                List.of(new CurrencyRestDto("AFN", "Afghan afghani", "؋"))));
  }

  @Test
  void serverError_isUnavailable() {
    server
        .expect(ExpectedCount.times(3), requestTo(BASE_URL + "/timezones"))
        .andRespond(withServerError());

    assertThatExceptionOfType(CountriesApiUnavailableException.class)
        .isThrownBy(() -> client.getTimezones());
  }

  @Test
  void tooManyRequests_isUnavailable() {
    server
        .expect(ExpectedCount.times(3), requestTo(BASE_URL + "/timezones"))
        .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

    assertThatExceptionOfType(CountriesApiUnavailableException.class)
        .isThrownBy(() -> client.getTimezones());
  }

  @Test
  void ioFailure_isUnavailable() {
    for (int attempt = 0; attempt < 3; attempt++) {
      server
          .expect(requestTo(BASE_URL + "/timezones"))
          .andRespond(request -> {
            throw new IOException("connection reset");
          });
    }

    assertThatExceptionOfType(CountriesApiUnavailableException.class)
        .isThrownBy(() -> client.getTimezones());
  }

  @Test
  void notFound_isRequestExceptionCarryingStatus() {
    server.expect(requestTo(BASE_URL + "/timezones")).andRespond(withResourceNotFound());

    assertThatExceptionOfType(CountriesApiRequestException.class)
        .isThrownBy(() -> client.getTimezones())
        .satisfies(e -> assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND));
  }

  @Test
  void malformedBody_isResponseException() {
    server
        .expect(requestTo(BASE_URL + "/timezones"))
        .andRespond(withSuccess("{not json", MediaType.APPLICATION_JSON));

    assertThatExceptionOfType(CountriesApiResponseException.class)
        .isThrownBy(() -> client.getTimezones());
  }

  @Test
  void exceptionMessage_containsPathAndPaging() {
    server.expect(ExpectedCount.times(3), queryParam("limit", "10")).andRespond(withServerError());

    assertThatExceptionOfType(CountriesApiUnavailableException.class)
        .isThrownBy(() -> client.getLanguages(10, 30))
        .withMessageContaining("/languages")
        .withMessageContaining("limit=10")
        .withMessageContaining("offset=30");
  }

  @Test
  void transientFailures_areRetriedUntilSuccess() {
    server.expect(requestTo(BASE_URL + "/timezones")).andRespond(withServerError());
    server
        .expect(requestTo(BASE_URL + "/timezones"))
        .andRespond(request -> {
          throw new IOException("connection reset");
        });
    server
        .expect(requestTo(BASE_URL + "/timezones"))
        .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

    assertThat(client.getTimezones()).isEmpty();
  }

  @Test
  void notFound_isNotRetried() {
    server
        .expect(ExpectedCount.once(), requestTo(BASE_URL + "/timezones"))
        .andRespond(withResourceNotFound());

    assertThatExceptionOfType(CountriesApiRequestException.class)
        .isThrownBy(() -> client.getTimezones());
  }
}
