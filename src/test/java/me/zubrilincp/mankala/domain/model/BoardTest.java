package me.zubrilincp.mankala.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.validation.board.IllegalBoardArgumentException;
import me.zubrilincp.mankala.domain.exception.validation.board.InvalidPitsSetupException;
import org.junit.jupiter.api.Test;

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
    assertDoesNotThrow(() -> new Board(pits));
  }

  @Test
  public void givenPitsNull_whenInitializingBoard_thenNoExceptionIsThrown() {
    // Arrange & Act
    Throwable exception = assertThrowsExactly(IllegalBoardArgumentException.class, () -> new Board(null));

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
    Throwable exception = assertThrowsExactly(IllegalBoardArgumentException.class, () -> new Board(pits));

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
    Throwable exception = assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits));

    // Assert
    assertEquals("Pits must have STORE pit as last pit for each player", exception.getMessage());
  }

  @Test
  public void givenInvalidPitsNoStore_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));

    // Act
    Throwable exception = assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits));

    // Assert
    assertEquals("Pits must have 1 store pit for each player", exception.getMessage());
  }

  @Test
  public void givenInvalidPitsSeveralStores_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));

    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));

    // Act
    Throwable exception = assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits));

    // Assert
    assertEquals("Pits must have 1 store pit for each player", exception.getMessage());
  }

  @Test
  public void givenInvalidPitsNoHousePit_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));

    // Act
    Throwable exception = assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits));

    // Assert
    assertEquals("Pits must have at least 1 house pit for each player", exception.getMessage());
  }

  @Test
  public void givenUnequalPitsCountPerPlayer_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    pits.add(new Pit(Player.PLAYER_ONE, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_ONE, PitType.STORE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.HOUSE, 6));
    pits.add(new Pit(Player.PLAYER_TWO, PitType.STORE, 6));

    // Act
    Throwable exception = assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits));

    // Assert
    assertEquals("Players must have the same number of pits", exception.getMessage());
  }
}