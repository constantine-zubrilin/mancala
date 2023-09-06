package me.zubrilincp.mankala.domain.model;

import static me.zubrilincp.mankala.util.mother.BoardMother.aBoard;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.zubrilincp.mankala.domain.commons.PartyState;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.validation.party.IllegalPartyArgumentException;
import org.junit.jupiter.api.Test;

class PartyTest {
  // TODO: split test

  @Test
  public void givenValidArguments_whenCreatingParty_thenNoExceptionIsThrown() {
    // Arrange
    UUID id = UUID.randomUUID();
    PartyState state = PartyState.IN_PROGRESS;
    Board board = aBoard();
    Player playerTurn = Player.PLAYER_ONE;

    // Act & Assert
    assertDoesNotThrow(() -> new Party(id, state, board, playerTurn));
  }

  @Test
  public void givenNullId_whenCreatingParty_thenIllegalArgumentExceptionIsThrown() {
    // Arrange
    PartyState state = PartyState.IN_PROGRESS;
    Board board = aBoard();
    Player playerTurn = Player.PLAYER_ONE;

    // Act
    Throwable exception =
        assertThrowsExactly(
            IllegalPartyArgumentException.class, () -> new Party(null, state, board, playerTurn));

    // Assert
    assertEquals("id cannot be null", exception.getMessage());
  }

  @Test
  public void givenNullState_whenCreatingParty_thenIllegalArgumentExceptionIsThrown() {
    // Arrange
    UUID id = UUID.randomUUID();
    Board board = aBoard();
    Player playerTurn = Player.PLAYER_ONE;

    // Act
    Throwable exception =
        assertThrowsExactly(
            IllegalPartyArgumentException.class, () -> new Party(id, null, board, playerTurn));

    // Assert
    assertEquals("state cannot be null", exception.getMessage());
  }

  @Test
  public void givenNullBoard_whenCreatingParty_thenIllegalArgumentExceptionIsThrown() {
    // Arrange
    UUID id = UUID.randomUUID();
    PartyState state = PartyState.IN_PROGRESS;
    Player playerTurn = Player.PLAYER_ONE;

    // Act
    Throwable exception =
        assertThrowsExactly(
            IllegalPartyArgumentException.class, () -> new Party(id, state, null, playerTurn));

    // Assert
    assertEquals("board cannot be null", exception.getMessage());
  }

  @Test
  public void givenNullPlayerTurn_whenCreatingParty_thenIllegalArgumentExceptionIsThrown() {
    // Arrange
    UUID id = UUID.randomUUID();
    PartyState state = PartyState.IN_PROGRESS;
    Board board = aBoard();

    // Act
    Throwable exception =
        assertThrowsExactly(
            IllegalPartyArgumentException.class, () -> new Party(id, state, board, null));

    // Assert
    assertEquals("player turn cannot be null", exception.getMessage());
  }

  @Test
  public void
      givenPartyAndPlayerMakesMove_whenLastStoneEndsOnOtherPlayerHouse_thenPlayerTurnChanges() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 0));

    Board board = new Board(pits, null);
    Party party = new Party(UUID.randomUUID(), PartyState.IN_PROGRESS, board, Player.PLAYER_ONE);

    // Act
    Party newParty = party.makeMove(0);

    // Assert
    Board newBoard = newParty.board();
    assertEquals(Player.PLAYER_TWO, newParty.playerTurn());
    assertEquals(PartyState.IN_PROGRESS, newParty.state());
    assertEquals(0, newBoard.pits().get(0).stones());
    assertEquals(1, newBoard.pits().get(1).stones());
    assertEquals(3, newBoard.pits().get(2).stones());
    assertEquals(0, newBoard.pits().get(3).stones());
  }

  @Test
  public void
      givenPartyAndPlayerMakesMove_whenLastStoneEndsOnStore_thenPlayerMakesAdditionalMove() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 4));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 4));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 0));

    Board board = new Board(pits, null);
    Party party = new Party(UUID.randomUUID(), PartyState.IN_PROGRESS, board, Player.PLAYER_ONE);

    // Act
    Party newParty = party.makeMove(0);

    // Assert
    Board newBoard = newParty.board();
    assertEquals(Player.PLAYER_ONE, newParty.playerTurn());
    assertEquals(PartyState.IN_PROGRESS, newParty.state());
    assertEquals(1, newBoard.pits().get(0).stones());
    assertEquals(2, newBoard.pits().get(1).stones());
    assertEquals(5, newBoard.pits().get(2).stones());
    assertEquals(0, newBoard.pits().get(3).stones());
  }

  @Test
  public void
      givenPartyAndPlayerMakesMove_whenLastStoneEndsOnEmptyPlayersHouse_thenPlayerPutsStonesFromOppositeSide() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 0));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 4));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 4));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 0));

    Board board = new Board(pits, null);
    Party party = new Party(UUID.randomUUID(), PartyState.IN_PROGRESS, board, Player.PLAYER_ONE);

    // Act
    Party newParty = party.makeMove(1);

    // Assert
    Board newBoard = newParty.board();
    assertEquals(Player.PLAYER_TWO, newParty.playerTurn());
    assertEquals(PartyState.IN_PROGRESS, newParty.state());
    assertEquals(0, newBoard.pits().get(0).stones());
    assertEquals(0, newBoard.pits().get(1).stones());
    assertEquals(7, newBoard.pits().get(2).stones());
    assertEquals(1, newBoard.pits().get(3).stones());
    assertEquals(0, newBoard.pits().get(4).stones());
    assertEquals(0, newBoard.pits().get(5).stones());
  }
}
