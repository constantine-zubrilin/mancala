package me.zubrilincp.mankala.application.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import me.zubrilincp.mankala.adapter.config.PartyProperties;
import me.zubrilincp.mankala.application.port.in.ManagePartyUseCase;
import me.zubrilincp.mankala.application.port.in.PlayerMoveUseCase;
import me.zubrilincp.mankala.application.port.out.persistence.LoadPartyPort;
import me.zubrilincp.mankala.application.port.out.persistence.SavePartyPort;
import me.zubrilincp.mankala.domain.commons.PartyState;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.model.Board;
import me.zubrilincp.mankala.domain.model.Party;
import me.zubrilincp.mankala.domain.model.Pit;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class PartyService implements ManagePartyUseCase, PlayerMoveUseCase {

  private final PartyProperties partyProperties;
  private final SavePartyPort savePartyPort;
  private final LoadPartyPort loadPartyPort;

  @Override
  @Transactional
  public Party createParty() {
    List<Pit> pits = new ArrayList<>();
    Arrays.stream(Player.values())
        .forEach(
            player -> {
              for (int i = 0; i < partyProperties.getNumberOfHomePits(); i++) {
                pits.add(
                    new Pit(
                        player, PitType.HOUSE, partyProperties.getInitialNumberOfStonesPerPit()));
              }
              pits.add(new Pit(player, PitType.STORE, 0));
            });

    Party party =
        new Party(
            UUID.randomUUID(), PartyState.IN_PROGRESS, new Board(pits, null), Player.PLAYER_ONE, 0);
    return savePartyPort.saveParty(party);
  }

  @Override
  public Party findParty(UUID partyId) {
    return loadPartyPort.loadParty(partyId);
  }

  @Override
  @Transactional
  public Party playerMove(UUID partyId, Player player, int pitIndex) {
    Party party = loadPartyPort.loadParty(partyId);
    Party partyAfterMove = party.makeMove(player, pitIndex);
    return this.savePartyPort.saveParty(partyAfterMove);
  }
}
