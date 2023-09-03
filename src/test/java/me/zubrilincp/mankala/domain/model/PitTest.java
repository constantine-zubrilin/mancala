package me.zubrilincp.mankala.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.validation.pit.IllegalPitArgumentException;
import org.junit.jupiter.api.Test;

class PitTest {
  @Test
  public void givenValidValues_whenCreatingPit_thenPropertiesAreSetCorrectly() {
    // Arrange
    Player player = Player.PLAYER_TWO;
    PitType type = PitType.HOUSE;
    int stones = 4;

    // Act
    Pit pit = new Pit(player, type, stones);

    // Assert
    assertEquals(player, pit.player());
    assertEquals(type, pit.type());
    assertEquals(stones, pit.stones());
  }

  @Test
  public void givenNullPlayer_whenCreatingPit_thenThrowIllegalArgumentException() {
    // Arrange
    PitType type = PitType.HOUSE;
    int stones = 4;

    // Act & Assert
    assertThrowsExactly(IllegalPitArgumentException.class, () -> {
      new Pit(null, type, stones);
    });
  }

  @Test
  public void givenNullType_whenCreatingPit_thenThrowIllegalArgumentException() {
    // Arrange
    Player player = Player.PLAYER_TWO;
    int stones = 4;

    // Act & Assert
    assertThrowsExactly(IllegalPitArgumentException.class, () -> {
      new Pit(player, null, stones);
    });
  }

  @Test
  public void givenNegativeeStones_whenCreatingPit_thenThrowIllegalArgumentException() {
    // Arrange
    Player player = Player.PLAYER_TWO;
    PitType type = PitType.HOUSE;
    int stones = -1;

    // Act & Assert
    assertThrowsExactly(IllegalPitArgumentException.class, () -> {
      new Pit(player, type, stones);
    });
  }
}