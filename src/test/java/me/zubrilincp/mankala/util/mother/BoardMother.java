package me.zubrilincp.mankala.util.mother;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.model.Board;
import me.zubrilincp.mankala.domain.model.Pit;

public class BoardMother {

  public static Board aBoard() {
    List<Pit> validPits = new ArrayList<>();
    for (Player player : Player.values()) {
      validPits.add(new Pit(player, PitType.HOUSE, 6));
      validPits.add(new Pit(player, PitType.STORE, 6));
    }

    return new Board(validPits);
  }
}
