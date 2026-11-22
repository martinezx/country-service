package org.xmdf.countryservice.domain;

/**
 * A geographic region, such as Europe or Asia.
 *
 * @param id the identifier assigned when the region is stored, or null if it has not been stored
 * @param name the name of the region, which identifies it among the regions
 */
public record Region(Long id, String name) {}
