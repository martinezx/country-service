package org.xmdf.countryservice.adapter.out.batch.load;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.xmdf.countryservice.application.exception.InvalidRecordException;

public class StoreItemWriter<L, D> implements ItemWriter<JsonNode> {

  private final ObjectMapper objectMapper;
  private final Class<L> lineType;
  private final Function<L, D> toDomain;
  private final Consumer<List<D>> store;

  StoreItemWriter(
      ObjectMapper objectMapper,
      Class<L> lineType,
      Function<L, D> toDomain,
      Consumer<List<D>> store) {
    this.objectMapper = objectMapper;
    this.lineType = lineType;
    this.toDomain = toDomain;
    this.store = store;
  }

  @Override
  public void write(Chunk<? extends JsonNode> chunk) {
    store.accept(chunk.getItems().stream().map(node -> toDomain.apply(read(node))).toList());
  }

  private L read(JsonNode node) {
    try {
      return objectMapper.treeToValue(node, lineType);
    } catch (JsonProcessingException e) {
      throw new InvalidRecordException(
          "Invalid %s line: %s".formatted(lineType.getSimpleName(), node), e);
    }
  }
}
