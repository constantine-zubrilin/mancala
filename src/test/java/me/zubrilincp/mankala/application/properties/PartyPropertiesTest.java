package me.zubrilincp.mankala.application.properties;

import static org.junit.jupiter.api.Assertions.*;

import me.zubrilincp.mankala.adapter.config.PartyProperties;
import me.zubrilincp.mankala.domain.exception.validation.party.IllegalPartyPropertiesException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PartyPropertiesTest {
  @Test
  public void givenValidProperties_whenCreatingPartyProperties_thenNoExceptionIsThrown() {
    assertDoesNotThrow(() -> new PartyProperties(1, 1));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  public void
      givenNonPositiveNumberOfHomePits_whenCreatingPartyProperties_thenIllegalPartyPropertiesExceptionIsThrown(
          int numberOfHomePits) {
    // Arrange & Act
    Throwable exception =
        assertThrows(
            IllegalPartyPropertiesException.class, () -> new PartyProperties(numberOfHomePits, 1));

    // Assert
    assertEquals("Number of home pits must be greater than 0", exception.getMessage());
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  public void
      givenNonPositiveNumberOfStones_whenCreatingPartyProperties_thenIllegalPartyPropertiesExceptionIsThrown(
          int numberOfStones) {
    // Arrange & Act
    Throwable exception =
        assertThrows(
            IllegalPartyPropertiesException.class, () -> new PartyProperties(1, numberOfStones));

    // Assert
    assertEquals("Number of stones must be greater than 0", exception.getMessage());
  }
}
