package me.zubrilincp.mankala.domain.model;

import static me.zubrilincp.mankala.util.mother.BoardMother.aBoard;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.zubrilincp.mankala.domain.commons.PartyState;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.InvalidPlayerTurnException;
import me.zubrilincp.mankala.domain.exception.PartyIsNotInProgressException;
import me.zubrilincp.mankala.domain.exception.validation.party.IllegalPartyArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

class PartyTest {
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
    Board board = Mockito.mock(Board.class);
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 0));
    when(board.makeMove(Player.PLAYER_ONE, 0)).thenReturn(new Board(pits, 2));

    Party party = new Party(UUID.randomUUID(), PartyState.IN_PROGRESS, board, Player.PLAYER_ONE);

    // Act
    Party newParty = party.makeMove(party.playerTurn(), 0);

    // Assert
    assertEquals(Player.PLAYER_TWO, newParty.playerTurn());
    assertEquals(PartyState.IN_PROGRESS, newParty.state());
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
    Party newParty = party.makeMove(party.playerTurn(), 0);

    // Assert
    assertEquals(Player.PLAYER_ONE, newParty.playerTurn());
    assertEquals(PartyState.IN_PROGRESS, newParty.state());
  }

  @Test
  public void
      givenPartyAndPlayerMakesMove_whenLastStoneEndsOnEmptyPlayersHouse_thenPlayerTurnChanges() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 4));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 4));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 0));

    Board board = new Board(pits, null);
    Party party = new Party(UUID.randomUUID(), PartyState.IN_PROGRESS, board, Player.PLAYER_ONE);

    // Act
    Party newParty = party.makeMove(party.playerTurn(), 1);

    // Assert
    assertEquals(Player.PLAYER_TWO, newParty.playerTurn());
    assertEquals(PartyState.IN_PROGRESS, newParty.state());
  }

  @Test
  public void givenPartyAndPlayerMakesMove_whenNoStonesInHouses_thenPartyFinishes() {
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
    Party newParty = party.makeMove(party.playerTurn(), 1);

    // Assert
    assertEquals(Player.PLAYER_TWO, newParty.playerTurn());
    assertEquals(PartyState.FINISHED, newParty.state());
  }

  @ParameterizedTest
  @EnumSource(
      value = PartyState.class,
      mode = EnumSource.Mode.EXCLUDE,
      names = {"IN_PROGRESS"})
  void givenPartyNotInProgressState_whenMakingMove_thenPartyIsNotInProgressExceptionIsThrown(
      PartyState partyState) {
    // Arrange
    Party party = new Party(UUID.randomUUID(), partyState, aBoard(), Player.PLAYER_ONE);

    // Act
    Throwable exception =
        assertThrowsExactly(
            PartyIsNotInProgressException.class, () -> party.makeMove(Player.PLAYER_ONE, 0));

    // Assert
    assertEquals("Party is not in progress", exception.getMessage());
  }

  @Test
  void givenPartyWithPlayerOneTurn_whenPlayerTwoMakeMove_thenInvalidPlayerTurnExceptionIsThrown() {
    // Arrange
    Party party = new Party(UUID.randomUUID(), PartyState.IN_PROGRESS, aBoard(), Player.PLAYER_ONE);

    // Act
    Throwable exception =
        assertThrowsExactly(
            InvalidPlayerTurnException.class, () -> party.makeMove(Player.PLAYER_TWO, 0));

    // Assert
    assertEquals("Player cannot move, it's not theirs turn", exception.getMessage());
  }
}
