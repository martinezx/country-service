package org.xmdf.countryservice.adapter.in.web.openapi;

import static org.xmdf.countryservice.adapter.in.web.paging.PageParameters.DEFAULT_SIZE;
import static org.xmdf.countryservice.adapter.in.web.paging.PageParameters.MAX_SIZE;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.tags.Tag;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI countryServiceOpenApi(ObjectProvider<BuildProperties> buildProperties) {
    String version =
        buildProperties.stream().map(BuildProperties::getVersion).findFirst().orElse("dev");
    return new OpenAPI()
        .info(
            new Info()
                .title("Country Service API")
                .version(version)
                .description(
                    "Countries, regions, currencies, languages and timezones loaded from"
                        + " countries.dev. Representations are HAL, and resources are identified"
                        + " by their numeric id."));
  }

  @Bean
  public ParameterCustomizer pagingParameters() {
    return (parameter, methodParameter) -> {
      if ("page".equals(parameter.getName())) {
        parameter.setDescription("Zero-based index of the page to return.");
        parameter.setSchema(integer(0, null, 0));
      } else if ("size".equals(parameter.getName())) {
        parameter.setDescription("Maximum number of elements in the page.");
        parameter.setSchema(integer(1, MAX_SIZE, DEFAULT_SIZE));
      }
      return parameter;
    };
  }

  @Bean
  public OpenApiCustomizer displayOrder() {
    List<Tag> tags =
        List.of(
            tag(ApiTags.DATA_REFRESH, "Refreshes the stored data from countries.dev."),
            tag(ApiTags.ROOT, "Entry point of the API."),
            tag(
                ApiTags.COUNTRIES,
                "Countries and territories. Their region, currencies, languages and timezones"
                    + " are links."),
            tag(ApiTags.CURRENCIES, "Currencies and the countries that use them."),
            tag(ApiTags.LANGUAGES, "Languages and the countries where they are spoken."),
            tag(ApiTags.REGIONS, "Geographic regions, such as Europe or Asia."),
            tag(ApiTags.TIMEZONES, "Timezones and the countries that use them."));
    List<String> order = tags.stream().map(Tag::getName).toList();
    return openApi -> {
      openApi.setTags(tags);
      Paths sorted = new Paths();
      openApi.getPaths().entrySet().stream()
          .sorted(
              Comparator.comparingInt((Map.Entry<String, PathItem> path) -> position(path, order))
                  .thenComparing(Map.Entry::getKey))
          .forEach(path -> sorted.addPathItem(path.getKey(), path.getValue()));
      openApi.setPaths(sorted);
    };
  }

  private static Tag tag(String name, String description) {
    return new Tag().name(name).description(description);
  }

  private static int position(Map.Entry<String, PathItem> path, List<String> order) {
    return path.getValue().readOperations().stream()
        .flatMap(operation -> operation.getTags().stream())
        .mapToInt(order::indexOf)
        .min()
        .orElse(order.size());
  }

  private static IntegerSchema integer(int minimum, Integer maximum, int defaultValue) {
    IntegerSchema schema = new IntegerSchema();
    schema.setMinimum(BigDecimal.valueOf(minimum));
    if (maximum != null) {
      schema.setMaximum(BigDecimal.valueOf(maximum));
    }
    schema.setDefault(defaultValue);
    return schema;
  }
}
