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
    List<Pit> validPits = new ArrayList<>();
    for (Player player : Player.values()) {
      validPits.add(new Pit(player, PitType.HOUSE, 6));
      validPits.add(new Pit(player, PitType.STORE, 6));
    }

    // Act & Assert
    assertDoesNotThrow(() -> new Board(validPits));
  }

  @Test
  public void givenPitsNull_whenInitializingBoard_thenNoExceptionIsThrown() {
    // Arrange & Act
    Throwable exception = assertThrowsExactly(IllegalBoardArgumentException.class, () -> new Board(null));

    // Assert
    assertEquals("pits cannot be null", exception.getMessage());
  }

  @Test
  public void givenInvalidPitsNoStore_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    for (Player player : Player.values()) {
      pits.add(new Pit(player, PitType.HOUSE, 6));
    }

    // Act
    Throwable exception = assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits));

    // Assert
    assertEquals("pits must have 1 store pit for each player", exception.getMessage());
  }

  @Test
  public void givenInvalidPitsSeveralStores_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    for (Player player : Player.values()) {
      pits.add(new Pit(player, PitType.HOUSE, 6));
      pits.add(new Pit(player, PitType.STORE, 6));
      pits.add(new Pit(player, PitType.STORE, 6));
    }

    // Act
    Throwable exception = assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits));

    // Assert
    assertEquals("pits must have 1 store pit for each player", exception.getMessage());
  }

  @Test
  public void givenInvalidPitsNoHousePit_whenInitializingBoard_thenInvalidPitsConfigurationExceptionIsThrown() {
    // Arrange
    List<Pit> pits = new ArrayList<>();
    for (Player player : Player.values()) {
      pits.add(new Pit(player, PitType.STORE, 6));
    }

    // Act
    Throwable exception = assertThrowsExactly(InvalidPitsSetupException.class, () -> new Board(pits));

    // Assert
    assertEquals("pits must have at least 1 house pit for each player", exception.getMessage());
  }
}