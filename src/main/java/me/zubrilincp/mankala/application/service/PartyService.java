package me.zubrilincp.mankala.application.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import me.zubrilincp.mankala.application.port.in.NewPartyUseCase;
import me.zubrilincp.mankala.application.port.in.PlayerMoveUseCase;
import me.zubrilincp.mankala.application.port.out.persistence.LoadPartyPort;
import me.zubrilincp.mankala.application.port.out.persistence.SavePartyPort;
import me.zubrilincp.mankala.application.properties.PartyProperties;
import me.zubrilincp.mankala.domain.commons.PartyState;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.InvalidPitIndexException;
import me.zubrilincp.mankala.domain.exception.InvalidPitOwnerException;
import me.zubrilincp.mankala.domain.exception.InvalidPitStonesCountException;
import me.zubrilincp.mankala.domain.exception.InvalidPitTypeException;
import me.zubrilincp.mankala.domain.exception.InvalidPlayerTurnException;
import me.zubrilincp.mankala.domain.exception.PartyIsNotInProgressException;
import me.zubrilincp.mankala.domain.model.Board;
import me.zubrilincp.mankala.domain.model.Party;
import me.zubrilincp.mankala.domain.model.Pit;

@AllArgsConstructor
public class PartyService implements NewPartyUseCase, PlayerMoveUseCase {

  private final PartyProperties partyProperties;
  private final SavePartyPort savePartyPort;
  private final LoadPartyPort loadPartyPort;

  // TODO: add documentation
  @Override
  public Party createParty() {
    List pits = new ArrayList();
    Arrays.stream(Player.values()).forEach(player -> {
      for (int i = 0; i < partyProperties.numberOfHomePits(); i++) {
        pits.add(new Pit(player, PitType.HOUSE, partyProperties.numberOfStones()));
      }
      pits.add(new Pit(player, PitType.STORE, 0));
    });

    Party party = new Party(UUID.randomUUID(), PartyState.IN_PROGRESS, new Board(pits), Player.PLAYER_ONE);
    savePartyPort.saveParty(party);

    return party;
  }

  @Override
  // TODO: tests for this method
  public Party playerMove(UUID partyId, Player player, int pitIndex) {
    Party party = loadPartyPort.loadParty(partyId);

    // Validate business rules
    if (party.state() != PartyState.IN_PROGRESS) {
      throw new PartyIsNotInProgressException("Party is not in progress");
    }
    if (party.playerTurn() != player) {
      throw new InvalidPlayerTurnException("Player cannot move, it's not theirs turn");
    }
    if (pitIndex <= 0 || pitIndex >= party.board().pits().size() - 1) {
      throw new InvalidPitIndexException("Index must exist on the board");
    }
    if (party.board().pits().get(pitIndex).player() != player) {
      throw new InvalidPitOwnerException("Player cannot start move from this pit, it's not theirs");
    }
    if (party.board().pits().get(pitIndex).type() != PitType.HOUSE) {
      throw new InvalidPitTypeException("Player cannot start move from this pit, it's not HOME pit");
    }
    if (party.board().pits().get(pitIndex).stones() <= 0) {
      throw new InvalidPitStonesCountException("Player cannot start move from this pit, there is no stones");
    }
    // TODO: check concurrency?

    Party partyAfterMove = party.makeMove(pitIndex);

    this.savePartyPort.saveParty(partyAfterMove);

    return partyAfterMove;
  }

}
