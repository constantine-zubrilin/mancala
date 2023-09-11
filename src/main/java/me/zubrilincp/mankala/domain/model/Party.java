package me.zubrilincp.mankala.domain.model;

import java.util.UUID;
import me.zubrilincp.mankala.domain.commons.PartyState;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.InvalidPlayerTurnException;
import me.zubrilincp.mankala.domain.exception.PartyIsNotInProgressException;
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
  public Party makeMove(Player player, int pitIndex) {
    if (state() != PartyState.IN_PROGRESS) {
      throw new PartyIsNotInProgressException("Party is not in progress");
    }
    if (playerTurn() != player) {
      throw new InvalidPlayerTurnException("Player cannot move, it's not theirs turn");
    }

    Board updatedBoard = board.makeMove(player, pitIndex);
    Player nextTurnPlayer = getNextTurnPlayer(updatedBoard.lastUsedPit());
    PartyState partyState = noStonesInHouses(updatedBoard) ? PartyState.FINISHED : state();

    return new Party(id, partyState, updatedBoard, nextTurnPlayer);
  }

  /** Player can make move again if they ended on his store */
  private Player getNextTurnPlayer(Pit lastUsedPit) {
    if (lastUsedPit.type() == PitType.STORE) {
      return playerTurn;
    }
    return playerTurn == Player.PLAYER_ONE ? Player.PLAYER_TWO : Player.PLAYER_ONE;
  }

  private boolean noStonesInHouses(Board board) {
    return 0
        == board.pits().stream()
            .filter(pit -> pit.type() == PitType.HOUSE)
            .map(Pit::stones)
            .reduce(0L, Long::sum);
  }
}
