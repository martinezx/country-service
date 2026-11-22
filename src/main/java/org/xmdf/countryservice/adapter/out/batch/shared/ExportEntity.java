package org.xmdf.countryservice.adapter.out.batch.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum ExportEntity {
  REGION("region.ndjson", false),
  TIMEZONE("timezone.ndjson", false),
  CURRENCY("currency.ndjson", true),
  LANGUAGE("language.ndjson", true),
  COUNTRY("country.ndjson", true);

  private final String fileName;
  private final boolean chunked;
}
