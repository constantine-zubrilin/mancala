package me.zubrilincp.mankala.domain.exception.persistence;

public class FailedToSavePartyException extends RuntimeException {

  public FailedToSavePartyException(String message, Throwable cause) {
    super(message, cause);
  }
}
