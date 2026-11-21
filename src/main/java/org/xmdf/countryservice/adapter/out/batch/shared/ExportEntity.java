package org.xmdf.countryservice.adapter.out.batch.shared;

public enum ExportEntity {
  REGION("region.ndjson", false),
  TIMEZONE("timezone.ndjson", false),
  CURRENCY("currency.ndjson", true),
  LANGUAGE("language.ndjson", true),
  COUNTRY("country.ndjson", true);

  private final String fileName;
  private final boolean chunked;

  ExportEntity(String fileName, boolean chunked) {
    this.fileName = fileName;
    this.chunked = chunked;
  }

  public String fileName() {
    return fileName;
  }

  public boolean chunked() {
    return chunked;
  }
}
