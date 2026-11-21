package org.xmdf.countryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {

  private static final String COUNTRIES_API_DOMAIN = "https://countries.dev";
  private static final int RETRY_MAX_ATTEMPTS = 3;
  private static final long RETRY_INITIAL_INTERVAL_MS = 500;
  private static final double RETRY_MULTIPLIER = 2.0;
  private static final long RETRY_MAX_INTERVAL_MS = 5_000;

  @Bean
  public RestClient restClient(RestClient.Builder builder) {
    return builder.baseUrl(COUNTRIES_API_DOMAIN).build();
  }

  @Bean
  public RetryTemplate countriesRetryTemplate() {
    return countriesRetryTemplate(RETRY_INITIAL_INTERVAL_MS);
  }

  public static RetryTemplate countriesRetryTemplate(long initialIntervalMs) {
    return RetryTemplate.builder()
        .maxAttempts(RETRY_MAX_ATTEMPTS)
        .exponentialBackoff(initialIntervalMs, RETRY_MULTIPLIER, RETRY_MAX_INTERVAL_MS)
        .retryOn(ResourceAccessException.class)
        .retryOn(HttpServerErrorException.class)
        .retryOn(HttpClientErrorException.TooManyRequests.class)
        .build();
  }
}
