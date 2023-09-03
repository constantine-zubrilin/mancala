package me.zubrilincp.mankala.domain.model;

import java.util.List;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.validation.board.IllegalBoardArgumentException;
import me.zubrilincp.mankala.domain.exception.validation.board.InvalidPitsSetupException;

public record Board(List<Pit> pits) {

  public Board(List<Pit> pits) {
    if (pits == null) {
      throw new IllegalBoardArgumentException("Pits cannot be null");
    }

    pits.forEach(pit -> {
      if (pit == null) {
        throw new IllegalBoardArgumentException("Pits cannot contain null");
      }
    });

    if (pits.stream().filter(pit -> pit.player() == Player.PLAYER_ONE).count() != pits.stream().filter(pit -> pit.player() == Player.PLAYER_TWO).count()) {
      throw new InvalidPitsSetupException("Players must have the same number of pits");
    }

    validatePits(pits.subList(0, pits.size() / 2), Player.PLAYER_ONE);
    validatePits(pits.subList(pits.size() / 2, pits.size()), Player.PLAYER_TWO);

    this.pits = pits.stream().map(pit -> new Pit(pit.player(), pit.type(), pit.stones())).toList();
  }

  private void validatePits(List<Pit> pits, Player player) {
    pits.stream().map(pit -> {
      if (pit.player() != player) {
        throw new InvalidPitsSetupException("Pits must be for the same player");
      }
      return pit;
    });
    if (pits.stream().filter(pit -> pit.type() == PitType.STORE).count() != 1) {
      throw new InvalidPitsSetupException("Pits must have 1 store pit for each player");
    }
    if (pits.stream().filter(pit -> pit.type() == PitType.HOUSE).count() == 0) {
      throw new InvalidPitsSetupException("Pits must have at least 1 house pit for each player");
    }
    if (pits.get(pits.size() - 1).type() != PitType.STORE) {
      throw new InvalidPitsSetupException("Pits must have STORE pit as last pit for each player");
    }
  }

  @Override
  public List<Pit> pits() {
    return pits.stream().map(pit -> new Pit(pit.player(), pit.type(), pit.stones())).toList();
  }
}
