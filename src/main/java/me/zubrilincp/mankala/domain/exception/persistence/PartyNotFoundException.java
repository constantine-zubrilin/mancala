package me.zubrilincp.mankala.domain.exception.persistence;

public class PartyNotFoundException extends RuntimeException {

  public PartyNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
