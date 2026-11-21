package org.xmdf.countryservice.adapter.out.batch.shared;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "countries.export")
public record BatchSettings(@DefaultValue("./output") String outputDir) {

  public static final int CHUNK_SIZE = 50;
}
