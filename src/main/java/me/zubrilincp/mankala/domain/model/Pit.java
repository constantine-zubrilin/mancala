package me.zubrilincp.mankala.domain.model;

import me.zubrilincp.mankala.domain.commons.PitType;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.exception.validation.pit.IllegalPitArgumentException;

public record Pit(Player player, PitType type, int stones) {

  public Pit {
    if (player == null) {
      throw new IllegalPitArgumentException("player cannot be null");
    }
    if (type == null) {
      throw new IllegalPitArgumentException("type cannot be null");
    }
    if (stones < 0) {
      throw new IllegalPitArgumentException("stones cannot be negative");
    }
  }
}
