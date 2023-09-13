package me.zubrilincp.mankala.adapter.in.web.request;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.validation.request.IllegalPitIndexParamException;
import me.zubrilincp.mankala.domain.exception.validation.request.IllegalPlayerParamException;
import org.junit.jupiter.api.Test;

class PlayerMoveRequestTest {
  @Test
  void givenValidParameters_whenCreatingPlayerMoveRequest_thenNoExceptionIsThrown() {
    // Arrange & Act
    PlayerMoveRequest moveRequest = new PlayerMoveRequest(Player.PLAYER_TWO, 3);

    // Assert
    assertNotNull(moveRequest);
  }

  @Test
  void givenNullPlayer_whenCreatingPlayerMoveRequest_thenIllegalPlayerParamExceptionIsThrown() {
    assertThrows(
        IllegalPlayerParamException.class,
        () -> new PlayerMoveRequest(null, 3));
  }

  @Test
  void
      givenNegativePitIndex_whenCreatingPlayerMoveRequest_thenIllegalPitIndexParamExceptionIsThrown() {
    assertThrows(
        IllegalPitIndexParamException.class,
        () -> new PlayerMoveRequest(Player.PLAYER_TWO, -1));
  }
}
