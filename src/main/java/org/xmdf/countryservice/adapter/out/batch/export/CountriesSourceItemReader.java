package org.xmdf.countryservice.adapter.out.batch.export;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import org.springframework.batch.item.ItemReader;
import org.springframework.lang.Nullable;

public class CountriesSourceItemReader implements ItemReader<JsonNode> {

  private final BiFunction<Integer, Integer, List<?>> fetcher;
  private final ObjectMapper objectMapper;
  private final boolean paged;
  private final int pageSize;

  private Iterator<?> current;
  private int offset;
  private boolean exhausted;

  public CountriesSourceItemReader(
      BiFunction<Integer, Integer, List<?>> fetcher,
      ObjectMapper objectMapper,
      boolean paged,
      int pageSize) {
    this.fetcher = fetcher;
    this.objectMapper = objectMapper;
    this.paged = paged;
    this.pageSize = pageSize;
  }

  @Override
  @Nullable
  public JsonNode read() {
    while (current == null || !current.hasNext()) {
      if (exhausted) {
        return null;
      }
      List<?> page = paged ? fetcher.apply(pageSize, offset) : fetcher.apply(null, null);
      if (!paged || page.isEmpty()) {
        exhausted = true;
      }
      offset += page.size();
      current = page.iterator();
    }
    return objectMapper.valueToTree(current.next());
  }
}
