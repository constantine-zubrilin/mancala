package me.zubrilincp.mankala.adapter.in.web;

import java.util.UUID;
import lombok.AllArgsConstructor;
import me.zubrilincp.mankala.adapter.in.web.request.PlayerMoveRequest;
import me.zubrilincp.mankala.application.port.in.CreatePartyUseCase;
import me.zubrilincp.mankala.application.port.in.PlayerMoveUseCase;
import me.zubrilincp.mankala.domain.exception.InvalidPitOwnerException;
import me.zubrilincp.mankala.domain.exception.persistence.PartyNotFoundException;
import me.zubrilincp.mankala.domain.model.Party;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(
    value = {"/api/v1/party"},
    produces = {MediaType.APPLICATION_JSON_VALUE})
// TODO: add openapi documentation
@AllArgsConstructor
// TODO: add integration tests
public class PartyController {

  private final CreatePartyUseCase createPartyUseCase;
  private final PlayerMoveUseCase playerMoveUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  ResponseEntity<Party> createParty() {
    try {
      return new ResponseEntity<>(createPartyUseCase.createParty(), HttpStatus.CREATED);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
  }

  @PostMapping("/{partyId}/move")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  ResponseEntity<Party> playerMove(
      @PathVariable UUID partyId,
      @RequestBody PlayerMoveRequest playerMoveRequest) {
    try {
      return new ResponseEntity<>(playerMoveUseCase.playerMove(partyId,
          playerMoveRequest.player(), playerMoveRequest.pitIndex()),
          HttpStatus.OK);
    } catch (PartyNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    } catch (InvalidPitOwnerException e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
  }

}
