package me.zubrilincp.mankala.domain.model;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import me.zubrilincp.mankala.domain.commons.PartyState;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.FailedToFindPlayerStoreException;
import me.zubrilincp.mankala.domain.exception.validation.party.IllegalPartyArgumentException;

public record Party(UUID id, PartyState state, Board board, Player playerTurn) {


  // TODO: add created/updated at
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

  // TODO: tests for this method
  public Party makeMove(int pitIndex) {
    List<Pit> pits = board.pits();

    Pit startPit = pits.get(pitIndex);
    int stonesInHand = startPit.stones();
    pits.set(pitIndex, new Pit(startPit.player(), startPit.type(), 0));

    do {
      pitIndex = (pitIndex + 1) % pits.size();

      Pit pit = pits.get(pitIndex);
      if (pit.type() == PitType.STORE && pit.player() != playerTurn) {
        // Skip other player's STORE
        continue;
      }

      pits.set(pitIndex, new Pit(pit.player(), pit.type(), pit.stones() + 1));
      stonesInHand--;
    } while (stonesInHand > 0);

    Player nextTurnPlayer = getNextTurnPlayer(pits, pitIndex);
    takeOppositeStones(pits, pitIndex);

    // TODO: check party state
    PartyState partyState = state;

    return new Party(id, partyState, new Board(pits), nextTurnPlayer);
  }

  /**
   * Player can make move again if they ended on his store
   */
  private Player getNextTurnPlayer(List<Pit> pits, int pitIndex) {
    if (pits.get(pitIndex).type() == PitType.STORE) {
      return playerTurn;
    }
    return playerTurn == Player.PLAYER_ONE ? Player.PLAYER_TWO : Player.PLAYER_ONE;
  }

  /**
   * Player take all stones from opposite pit if they ended on theirs empty pit
   */
  private void takeOppositeStones(List<Pit> pits, int pitIndex) {
    Pit lastPit = pits.get(pitIndex);

    if (lastPit.player() == playerTurn && lastPit.type() == PitType.HOUSE && lastPit.stones() == 1) {
      int oppositePitIndex = pits.size() - pitIndex - 2;
      Pit oppositePit = pits.get(oppositePitIndex);

      if (oppositePit.player() != playerTurn && oppositePit.type() == PitType.HOUSE) {
        pits.set(pitIndex, new Pit(lastPit.player(), lastPit.type(), 0));
        pits.set(oppositePitIndex, new Pit(oppositePit.player(), oppositePit.type(), 0));

        pits.stream()
            .filter(pit1 -> pit1.player() == playerTurn && pit1.type() == PitType.STORE)
            .findFirst()
            .map(store -> new Pit(store.player(), store.type(),
                store.stones() + lastPit.stones() + oppositePit.stones()))
            .orElseThrow(() -> new FailedToFindPlayerStoreException("Failed to find player store"));
      }
    }
  }
}
