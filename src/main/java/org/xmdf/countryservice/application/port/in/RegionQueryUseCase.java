package org.xmdf.countryservice.application.port.in;

import java.util.List;
import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.domain.Region;

/** Use case that reads the stored regions. */
public interface RegionQueryUseCase {

  /**
   * Returns all regions.
   *
   * @return the regions ordered by name, empty if there are none
   */
  List<Region> findAll();

  /**
   * Returns a region.
   *
   * @param id the identifier of the region
   * @return the region
   * @throws ResourceNotFoundException if there is no region with that identifier
   */
  Region getById(Long id);
}
