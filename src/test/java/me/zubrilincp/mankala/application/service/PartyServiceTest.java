package me.zubrilincp.mankala.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.util.List;
import me.zubrilincp.mankala.application.port.out.persistence.LoadPartyPort;
import me.zubrilincp.mankala.application.port.out.persistence.SavePartyPort;
import me.zubrilincp.mankala.application.properties.PartyProperties;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.model.Pit;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PartyServiceTest {

  @Test
  void givenPartyConfiguration_whenCreatingParty_thenPartyIsCreated() {
    // Arrange
    PartyProperties partyProperties = new PartyProperties(6, 6);
    LoadPartyPort loadPartyPort = Mockito.mock(LoadPartyPort.class);
    SavePartyPort savePartyPort = Mockito.mock(SavePartyPort.class);
    PartyService partyService = new PartyService(partyProperties, savePartyPort, loadPartyPort);

    // Act
    var party = partyService.createParty();

    // Assert
    assertNotNull(party);
    verify(savePartyPort).saveParty(party);

    List<Pit> pits = party.board().pits();

    assertEquals(
        (partyProperties.numberOfHomePits() + 1) * Player.values().length,
        pits.size(),
        "Number of pits is correct");
    assertThat(pits.stream().filter(pit -> pit.type().equals(PitType.HOUSE)))
        .as("All pits of type HOUSE have the same number of stones")
        .extracting(Pit::stones)
        .contains(partyProperties.numberOfStones());
    assertThat(pits.stream().filter(pit -> pit.type() == PitType.STORE))
        .as("STORE pits have 0 stones")
        .extracting(Pit::stones)
        .contains(0);
  }
}
