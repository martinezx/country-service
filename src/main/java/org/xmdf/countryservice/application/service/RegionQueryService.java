package org.xmdf.countryservice.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.xmdf.countryservice.application.exception.ResourceNotFoundException;
import org.xmdf.countryservice.application.port.in.RegionQueryUseCase;
import org.xmdf.countryservice.application.port.out.RegionRepository;
import org.xmdf.countryservice.domain.Region;

@Service
public class RegionQueryService implements RegionQueryUseCase {

  private final RegionRepository regions;

  public RegionQueryService(RegionRepository regions) {
    this.regions = regions;
  }

  @Override
  public List<Region> findAll() {
    return regions.findAll();
  }

  @Override
  public Region getById(Long id) {
    return regions.findById(id).orElseThrow(() -> new ResourceNotFoundException("Region", id));
  }
}
