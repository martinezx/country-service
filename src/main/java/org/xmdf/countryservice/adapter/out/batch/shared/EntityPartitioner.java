package org.xmdf.countryservice.adapter.out.batch.shared;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class EntityPartitioner implements Partitioner {

  public static final String ENTITY_KEY = "entity";

  private final List<ExportEntity> entities;

  public EntityPartitioner(ExportEntity... entities) {
    this.entities = List.of(entities);
  }

  @Override
  public Map<String, ExecutionContext> partition(int gridSize) {
    Map<String, ExecutionContext> partitions = new LinkedHashMap<>();
    for (ExportEntity entity : entities) {
      ExecutionContext context = new ExecutionContext();
      context.putString(ENTITY_KEY, entity.name());
      partitions.put(entity.name(), context);
    }
    return partitions;
  }
}
