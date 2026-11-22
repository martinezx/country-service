package org.xmdf.countryservice.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class NaturalKeyUpserter<D, E, K> {

  private final EntityManager entityManager;
  private final Function<D, K> domainKey;
  private final Function<E, K> entityKey;
  private final Function<Collection<K>, List<E>> finder;
  private final Supplier<E> factory;
  private final BiConsumer<D, E> mapper;

  void upsertAll(Collection<D> items) {
    Map<K, D> byKey = new LinkedHashMap<>();
    for (D item : items) {
      byKey.put(domainKey.apply(item), item);
    }

    Map<K, E> existing = new LinkedHashMap<>();
    for (E entity : finder.apply(byKey.keySet())) {
      existing.put(entityKey.apply(entity), entity);
    }

    byKey.forEach((key, item) -> {
      E entity = existing.get(key);
      if (entity == null) {
        entity = factory.get();
        mapper.accept(item, entity);
        entityManager.persist(entity);
      } else {
        mapper.accept(item, entity);
      }
    });
    entityManager.flush();
  }
}
