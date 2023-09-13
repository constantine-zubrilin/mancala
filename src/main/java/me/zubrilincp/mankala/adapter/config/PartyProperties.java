package me.zubrilincp.mankala.adapter.config;

import lombok.Getter;
import me.zubrilincp.mankala.domain.exception.validation.party.IllegalPartyPropertiesException;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zubrilincp.mankala")
@Getter
public class PartyProperties {

  private long numberOfHomePits;
  private long initialNumberOfStonesPerPit;

  // TODO: add integration test
  public PartyProperties(long numberOfHomePits, long initialNumberOfStonesPerPit) {
    if (numberOfHomePits < 1) {
      throw new IllegalPartyPropertiesException("Number of home pits must be greater than 0");
    }
    if (initialNumberOfStonesPerPit < 1) {
      throw new IllegalPartyPropertiesException("Number of stones must be greater than 0");
    }

    this.numberOfHomePits = numberOfHomePits;
    this.initialNumberOfStonesPerPit = initialNumberOfStonesPerPit;
  }
}
