package org.xmdf.countryservice.application.port.out;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

  /**
   * Finds all regions.
   *
   * @return the regions ordered by name, empty if there are none
   */
  List<Region> findAll();

  /**
   * Finds a region by its identifier.
   *
   * @param id the identifier of the region
   * @return the region, or empty if there is none with that identifier
   */
  Optional<Region> findById(Long id);

  /**
   * Tells whether a region with the given identifier exists.
   *
   * @param id the identifier of the region
   * @return true if the region exists
   */
  boolean existsById(Long id);
}
