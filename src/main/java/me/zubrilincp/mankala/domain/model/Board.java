package me.zubrilincp.mankala.domain.model;

import java.util.Arrays;
import java.util.List;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.validation.board.IllegalBoardArgumentException;
import me.zubrilincp.mankala.domain.exception.validation.board.InvalidPitsSetupException;

public record Board(List<Pit> pits) {

  public Board {
    if (pits == null) {
      throw new IllegalBoardArgumentException("pits cannot be null");
    }

    // TODO: check all pits are not null
    Arrays.stream(Player.values())
        .map(player ->
            pits
                .stream()
                .filter(pit -> pit.player() == player && pit.type() == PitType.STORE)
                .count())
        .forEach(count -> {
          if (count != 1) {
            throw new InvalidPitsSetupException(
                "pits must have 1 store pit for each player");
          }
        });
    Arrays.stream(Player.values()).map(player ->
            pits.stream()
                .filter(pit -> pit.player() == player && pit.type() == PitType.HOUSE)
                .count())
        .forEach(count -> {
          if (count < 1) {
            throw new InvalidPitsSetupException(
                "pits must have at least 1 house pit for each player");
          }
        });

  }
}
