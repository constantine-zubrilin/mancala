package me.zubrilincp.mankala.util.mother;

import java.util.ArrayList;
import java.util.List;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.model.Board;
import me.zubrilincp.mankala.domain.model.Pit;

public class BoardMother {

  public static Board aBoard() {
    List<Pit> pits = new ArrayList<>();
    for (Player player : Player.values()) {
      pits.add(new Pit(player, PitType.HOUSE, 4));
      pits.add(new Pit(player, PitType.STORE, 4));
    }

    return new Board(pits, null);
  }
}
