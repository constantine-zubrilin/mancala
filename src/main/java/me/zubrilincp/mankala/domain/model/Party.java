package me.zubrilincp.mankala.domain.model;

import java.util.UUID;
import me.zubrilincp.mankala.domain.commons.PartyState;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.validation.party.IllegalPartyArgumentException;

// TODO: add move number with optimistic lock to prevent collisions
public record Party(UUID id, PartyState state, Board board, Player playerTurn) {

  public Party {
    if (id == null) {
      throw new IllegalPartyArgumentException("id cannot be null");
    }
    if (state == null) {
      throw new IllegalPartyArgumentException("state cannot be null");
    }
    if (board == null) {
      throw new IllegalPartyArgumentException("board cannot be null");
    }
    if (playerTurn == null) {
      throw new IllegalPartyArgumentException("player turn cannot be null");
    }
  }

  // TODO: comments
  public Party makeMove(int pitIndex) {
    Board updatedBoard = board.makeMove(pitIndex);
    Player nextTurnPlayer = getNextTurnPlayer(updatedBoard.lastUsedPit());

    // TODO: check party state
    PartyState partyState = state;

    return new Party(id, partyState, updatedBoard, nextTurnPlayer);
  }

  /**
   * Player can make move again if they ended on his store
   */
  private Player getNextTurnPlayer(Pit lastUsedPit) {
    if (lastUsedPit.type() == PitType.STORE) {
      return playerTurn;
    }
    return playerTurn == Player.PLAYER_ONE ? Player.PLAYER_TWO : Player.PLAYER_ONE;
  }
}
