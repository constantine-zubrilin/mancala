package me.zubrilincp.mankala.domain.model;

import static me.zubrilincp.mankala.util.mother.BoardMother.aBoard;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import me.zubrilincp.mankala.domain.commons.PartyState;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.validation.party.IllegalPartyArgumentException;
import org.junit.jupiter.api.Test;

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
    Throwable exception = assertThrowsExactly(IllegalPartyArgumentException.class,
        () -> new Party(null, state, board, playerTurn));

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
    Throwable exception = assertThrowsExactly(IllegalPartyArgumentException.class,
        () -> new Party(id, null, board, playerTurn));

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
    Throwable exception = assertThrowsExactly(IllegalPartyArgumentException.class,
        () -> new Party(id, state, null, playerTurn));

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
    Throwable exception = assertThrowsExactly(IllegalPartyArgumentException.class,
        () -> new Party(id, state, board, null));

    // Assert
    assertEquals("player turn cannot be null", exception.getMessage());
  }
}