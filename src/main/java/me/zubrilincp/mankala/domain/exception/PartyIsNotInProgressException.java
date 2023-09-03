package me.zubrilincp.mankala.domain.exception;

public class PartyIsNotInProgressException extends RuntimeException {

  public PartyIsNotInProgressException(String message) {
    super(message);
  }
}
