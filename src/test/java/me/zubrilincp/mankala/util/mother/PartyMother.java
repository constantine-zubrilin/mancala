package me.zubrilincp.mankala.util.mother;

import static me.zubrilincp.mankala.util.mother.BoardMother.aBoard;

import java.util.UUID;
import me.zubrilincp.mankala.domain.commons.PartyState;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.model.Party;

public class PartyMother {

  public static Party aParty() {
    return new Party(UUID.randomUUID(), PartyState.IN_PROGRESS, aBoard(), Player.PLAYER_ONE, 0);
  }
}
