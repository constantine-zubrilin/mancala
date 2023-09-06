package me.zubrilincp.mankala.domain.exception;

public class InvalidPlayerTurnException extends RuntimeException {

  public InvalidPlayerTurnException(String message) {
    super(message);
  }
}
