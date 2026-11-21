package org.xmdf.countryservice.application.port.out;

import java.util.Collection;
import org.xmdf.countryservice.domain.Region;

/** Storage for {@link Region regions}. */
public interface RegionRepository {

  /**
   * Stores the given regions. A region is matched to an existing one by name: matches are updated
   * and the others are inserted. Stored regions that are not in the collection are left untouched.
   * If the collection contains several regions with the same name, the last one wins.
   *
   * @param regions the regions to store
   */
  void upsertAll(Collection<Region> regions);
}
