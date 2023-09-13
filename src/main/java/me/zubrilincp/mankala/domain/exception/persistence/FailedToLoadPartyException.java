package me.zubrilincp.mankala.domain.exception.persistence;

public class FailedToLoadPartyException extends RuntimeException {

  public FailedToLoadPartyException(String message, Throwable cause) {
    super(message, cause);
  }
}
