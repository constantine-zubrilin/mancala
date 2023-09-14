package me.zubrilincp.mankala.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.UUID;
import lombok.AllArgsConstructor;
import me.zubrilincp.mankala.adapter.in.web.request.PlayerMoveRequest;
import me.zubrilincp.mankala.application.port.in.ManagePartyUseCase;
import me.zubrilincp.mankala.application.port.in.PlayerMoveUseCase;
import me.zubrilincp.mankala.domain.exception.InvalidPitOwnerException;
import me.zubrilincp.mankala.domain.exception.persistence.PartyNotFoundException;
import me.zubrilincp.mankala.domain.model.Party;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@AllArgsConstructor
public class PartyController {

  private final ManagePartyUseCase managePartyUseCase;
  private final PlayerMoveUseCase playerMoveUseCase;

  @Operation(summary = "Create new game party")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Party created",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Party.class))
            })
      })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  ResponseEntity<Party> createParty() {
    try {
      return new ResponseEntity<>(managePartyUseCase.createParty(), HttpStatus.CREATED);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
  }

  @Operation(summary = "Fetch game party by id")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found party",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Party.class))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Party not found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(ref = "ErrorResponseSchema"))
            })
      })
  @GetMapping("/{partyId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  ResponseEntity<Party> findParty(@PathVariable UUID partyId) {
    try {
      return new ResponseEntity<>(managePartyUseCase.findParty(partyId), HttpStatus.OK);
    } catch (PartyNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
  }

  @Operation(summary = "Make a move in game party")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Move made",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Party.class))
            }),
        @ApiResponse(
            responseCode = "403",
            description = "Move forbidden",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(ref = "ErrorResponseSchema"))
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Party not found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(ref = "ErrorResponseSchema"))
            })
      })
  @PostMapping("/{partyId}/move")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  ResponseEntity<Party> playerMove(
      @PathVariable UUID partyId, @RequestBody PlayerMoveRequest playerMoveRequest) {
    try {
      return new ResponseEntity<>(
          playerMoveUseCase.playerMove(
              partyId, playerMoveRequest.player(), playerMoveRequest.pitIndex()),
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
