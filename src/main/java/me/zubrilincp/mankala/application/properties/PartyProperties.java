package me.zubrilincp.mankala.application.properties;

import me.zubrilincp.mankala.domain.exception.validation.party.IllegalPartyPropertiesException;

public record PartyProperties(long numberOfHomePits, long numberOfStones) {

  // TODO: add integration test
  public PartyProperties {
    if (numberOfHomePits < 1) {
      throw new IllegalPartyPropertiesException("Number of home pits must be greater than 0");
    }
    if (numberOfStones < 1) {
      throw new IllegalPartyPropertiesException("Number of stones must be greater than 0");
    }
  }
}
