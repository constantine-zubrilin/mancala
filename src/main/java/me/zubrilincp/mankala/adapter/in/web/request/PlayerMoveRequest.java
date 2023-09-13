package me.zubrilincp.mankala.adapter.in.web.request;

import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.validation.request.IllegalPitIndexParamException;
import me.zubrilincp.mankala.domain.exception.validation.request.IllegalPlayerParamException;

public record PlayerMoveRequest(Player player, int pitIndex) {

  public PlayerMoveRequest {
    if (player == null) {
      throw new IllegalPlayerParamException("Player cannot be null");
    }
    if (pitIndex < 0) {
      throw new IllegalPitIndexParamException("Pit index must be positive or zero");
    }
  }
}
