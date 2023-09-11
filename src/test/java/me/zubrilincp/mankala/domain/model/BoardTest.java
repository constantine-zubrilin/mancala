package me.zubrilincp.mankala.domain.model;

import static me.zubrilincp.mankala.util.mother.BoardMother.aBoard;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.ArrayList;
import java.util.List;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.InvalidPitOwnerException;
import me.zubrilincp.mankala.domain.exception.InvalidPitStonesCountException;
import me.zubrilincp.mankala.domain.exception.InvalidPitTypeException;
import me.zubrilincp.mankala.domain.exception.PitIndexOutOfBoundException;
import me.zubrilincp.mankala.domain.exception.validation.board.IllegalBoardArgumentException;
import me.zubrilincp.mankala.domain.exception.validation.board.InvalidPitsSetupException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BoardTest {
  @Test
  public void givenValidPits_whenInitializingBoard_thenNoExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));

    // Act & Assert
    assertDoesNotThrow(() -> new Board(pits, null));
  }

  @Test
  public void givenPitsNull_whenInitializingBoard_thenNoExceptionIsThrown() {
    // Arrange & Act
    Throwable exception =
        assertThrowsExactly(IllegalBoardArgumentException.class, () -> new Board(null, null));

    // Assert
    assertEquals("Pits cannot be null", exception.getMessage());
  }

  @Test
  public void givenPitsWithNullElements_whenInitializingBoard_thenNoExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));
    pits.add(null);

    // Act
    Throwable exception =
        assertThrowsExactly(IllegalBoardArgumentException.class, () -> new Board(pits, null));

    // Assert
    assertEquals("Pits cannot contain null", exception.getMessage());
  }

  @Test
  public void givenPitsWithWrongOrderElements_whenInitializingBoard_thenNoExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));

    // Act
    Throwable exception =
        assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits, null));

    // Assert
    assertEquals("Pits must have STORE pit as last pit for each player", exception.getMessage());
  }

  @Test
  public void
      givenInvalidPitsNoStore_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));

    // Act
    Throwable exception =
        assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits, null));

    // Assert
    assertEquals("Pits must have 1 store pit for each player", exception.getMessage());
  }

  @Test
  public void
      givenInvalidPitsSeveralStores_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));

    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));

    // Act
    Throwable exception =
        assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits, null));

    // Assert
    assertEquals("Pits must have 1 store pit for each player", exception.getMessage());
  }

  @Test
  public void
      givenInvalidPitsNoHousePit_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));

    // Act
    Throwable exception =
        assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits, null));

    // Assert
    assertEquals("Pits must have at least 1 house pit for each player", exception.getMessage());
  }

  @Test
  public void
      givenUnequalPitsCountPerPlayer_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));

    // Act
    Throwable exception =
        assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits, null));

    // Assert
    assertEquals("Players must have the same number of pits", exception.getMessage());
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 10})
  public void
      givenBoard_whenMoveStartingFromInvalidPitIndex_thenBoardMovePitIndexOutOfBoundExceptionIsThrown(
          int pitIndex) {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));
    Board board = new Board(pits, null);

    // Act
    Throwable exception =
        assertThrowsExactly(
            PitIndexOutOfBoundException.class, () -> board.makeMove(Player.PLAYER_ONE, pitIndex));

    // Assert
    assertEquals("Pit index is out of bounds", exception.getMessage());
  }

  @Test
  public void
      givenBoardAndPlayerMakesMove_whenLastStoneEndsOnOtherPlayerHouse_thenBoardIsInProperState() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 0));

    Board board = new Board(pits, null);

    // Act
    Board newBoard = board.makeMove(Player.PLAYER_ONE, 1);

    // Assert
    assertEquals(2, newBoard.pits().get(0).stones());
    assertEquals(0, newBoard.pits().get(1).stones());
    assertEquals(1, newBoard.pits().get(2).stones());
    assertEquals(3, newBoard.pits().get(3).stones());
    assertEquals(2, newBoard.pits().get(4).stones());
    assertEquals(0, newBoard.pits().get(5).stones());
  }

  @Test
  public void givenBoardAndPlayerMakesMove_whenLastStoneEndsOnStore_thenBoardIsInProperState() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 4));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 4));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 0));

    Board board = new Board(pits, null);

    // Act
    Board newBoard = board.makeMove(Player.PLAYER_ONE, 0);

    // Assert
    assertEquals(1, newBoard.pits().get(0).stones());
    assertEquals(2, newBoard.pits().get(1).stones());
    assertEquals(5, newBoard.pits().get(2).stones());
    assertEquals(0, newBoard.pits().get(3).stones());
  }

  @Test
  public void
      givenBoardAndPlayerMakesMove_whenLastStoneEndsOnEmptyPlayersHouse_thenPlayerPutsStonesFromOppositeSide() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 1));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 5));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 5));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 1));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 0));

    Board board = new Board(pits, null);

    // Act
    Board newBoard = board.makeMove(Player.PLAYER_ONE, 1);

    // Assert
    assertEquals(2, newBoard.pits().get(0).stones());
    assertEquals(0, newBoard.pits().get(1).stones());
    assertEquals(8, newBoard.pits().get(2).stones());
    assertEquals(0, newBoard.pits().get(3).stones());
    assertEquals(2, newBoard.pits().get(4).stones());
    assertEquals(0, newBoard.pits().get(5).stones());
  }

  @Test
  public void
      givenBoardAndPlayerMakesMove_whenPlayerOneDoesNotHaveStones_thenPlayersMoveStonesToStores() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 0));

    Board board = new Board(pits, null);

    // Act
    Board newBoard = board.makeMove(Player.PLAYER_ONE, 0);

    // Assert
    assertEquals(0, newBoard.pits().get(0).stones());
    assertEquals(1, newBoard.pits().get(1).stones());
    assertEquals(0, newBoard.pits().get(2).stones());
    assertEquals(3, newBoard.pits().get(3).stones());
  }

  @Test
  public void
      givenBoardAndPlayerMakesMove_whenOneOfPlayersDoesNotHaveStones_thenPlayersMoveStonesToStores() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 0));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 2));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 0));

    Board board = new Board(pits, null);

    // Act
    Board newBoard = board.makeMove(Player.PLAYER_TWO, 4);

    // Assert
    assertEquals(0, newBoard.pits().get(0).stones());
    assertEquals(0, newBoard.pits().get(1).stones());
    assertEquals(5, newBoard.pits().get(2).stones());
    assertEquals(0, newBoard.pits().get(3).stones());
    assertEquals(0, newBoard.pits().get(4).stones());
    assertEquals(1, newBoard.pits().get(5).stones());
  }

  @Test
  void
      givenBoard_whenPlayerOneStartsMoveOnPitBelongingToPlayerTwo_thenInvalidPitOwnerExceptionIsThrown() {
    // Arrange
    Board board = aBoard();

    // Act
    Throwable exception =
        assertThrowsExactly(
            InvalidPitOwnerException.class, () -> board.makeMove(Player.PLAYER_ONE, 2));

    // Assert
    assertEquals("Player cannot start move from this pit, it's not theirs", exception.getMessage());
  }

  @Test
  void givenBoard_whenPlayerStartsMoveOnNotHouse_thenInvalidPitTypeExceptionIsThrown() {
    // Arrange
    Board board = aBoard();

    // Act
    Throwable exception =
        assertThrowsExactly(
            InvalidPitTypeException.class, () -> board.makeMove(Player.PLAYER_ONE, 1));

    // Assert
    assertEquals(
        "Player cannot start move from this pit, it's not HOME pit", exception.getMessage());
  }

  @Test
  void
      givenBoard_whenPlayerStartsMoveOnHouseWithZeroStones_thenInvalidPitStonesCountExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    for (Player player : Player.values()) {
      pits.add(new Pit(player, PitType.HOUSE, 0));
      pits.add(new Pit(player, PitType.STORE, 4));
    }
    Board board = new Board(pits, null);

    // Act
    Throwable exception =
        assertThrowsExactly(
            InvalidPitStonesCountException.class, () -> board.makeMove(Player.PLAYER_ONE, 0));

    // Assert
    assertEquals(
        "Player cannot start move from this pit, there is no stones", exception.getMessage());
  }
}
