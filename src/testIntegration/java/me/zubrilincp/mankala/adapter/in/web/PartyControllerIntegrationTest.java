package me.zubrilincp.mankala.adapter.in.web;

import static me.zubrilincp.mankala.util.mother.PartyMother.aParty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import me.zubrilincp.mankala.application.port.in.ManagePartyUseCase;
import me.zubrilincp.mankala.application.port.in.PlayerMoveUseCase;
import me.zubrilincp.mankala.domain.exception.InvalidPitOwnerException;
import me.zubrilincp.mankala.domain.exception.persistence.FailedToSavePartyException;
import me.zubrilincp.mankala.domain.exception.persistence.PartyNotFoundException;
import me.zubrilincp.mankala.domain.model.Party;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class PartyControllerIntegrationTest {

  private MockMvc mockMvc;

  @Mock private ManagePartyUseCase managePartyUseCase;

  @Mock private PlayerMoveUseCase playerMoveUseCase;

  @BeforeEach
  public void setUp() {
    PartyController partyController = new PartyController(managePartyUseCase, playerMoveUseCase);
    mockMvc = MockMvcBuilders.standaloneSetup(partyController).setControllerAdvice().build();
  }

  @Test
  public void givenCreatePartyRequest_whenPartyCreated_thenReturnNewParty() throws Exception {
    Party party = aParty();

    when(managePartyUseCase.createParty()).thenReturn(party);

    mockMvc
        .perform(post("/api/v1/party").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(party.id().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(party.state().toString()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.pits[0].player")
                .value(party.board().pits().get(0).player().toString()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.pits[0].type")
                .value(party.board().pits().get(0).type().toString()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.pits[0].stones")
                .value(party.board().pits().get(0).stones()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.lastUsedPitIndex")
                .value(party.board().lastUsedPitIndex()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.playerTurn").value(party.playerTurn().toString()));
  }

  @Test
  public void givenCreatePartyRequest_whenPartyFailedToCreate_thenReturnErrorMessage()
      throws Exception {
    // Arrange
    Exception exception =
        new FailedToSavePartyException(
            "Failed to save party", new RuntimeException("Cause exception"));
    when(managePartyUseCase.createParty()).thenThrow(exception);

    // Act & Assert
    mockMvc
        .perform(post("/api/v1/party").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(status().reason(exception.getMessage()));
  }

  @Test
  public void givenPartyExists_whenPartyFetched_thenReturnParty() throws Exception {
    // Arrange
    Party party = aParty();
    when(managePartyUseCase.findParty(party.id())).thenReturn(party);

    // Act & Assert
    mockMvc
        .perform(get("/api/v1/party/{partyId}", party.id()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(party.id().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(party.state().toString()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.pits[0].player")
                .value(party.board().pits().get(0).player().toString()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.pits[0].type")
                .value(party.board().pits().get(0).type().toString()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.pits[0].stones")
                .value(party.board().pits().get(0).stones()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.lastUsedPitIndex")
                .value(party.board().lastUsedPitIndex()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.playerTurn").value(party.playerTurn().toString()));
  }

  @Test
  public void givenPartyDoesNotExist_whenPartyFetched_thenReturnErrorMessage() throws Exception {
    // Arrange
    Exception exception =
        new PartyNotFoundException("Party not found", new RuntimeException("Cause exception"));
    when(managePartyUseCase.findParty(any())).thenThrow(exception);

    // Act & Assert
    mockMvc
        .perform(get("/api/v1/party/{partyId}", UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(status().reason(exception.getMessage()));
  }

  @Test
  public void givenPartyExists_whenPlayerMoves_thenReturnParty() throws Exception {
    // Arrange
    Party party = aParty();
    when(playerMoveUseCase.playerMove(party.id(), party.playerTurn(), 0)).thenReturn(party);

    // Act & Assert
    mockMvc
        .perform(
            post("/api/v1/party/{partyId}/move", party.id())
                .content("{\"player\": \"PLAYER_ONE\",\"pitIndex\": 0}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(party.id().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(party.state().toString()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.pits[0].player")
                .value(party.board().pits().get(0).player().toString()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.pits[0].type")
                .value(party.board().pits().get(0).type().toString()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.pits[0].stones")
                .value(party.board().pits().get(0).stones()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.board.lastUsedPitIndex")
                .value(party.board().lastUsedPitIndex()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.playerTurn").value(party.playerTurn().toString()));
  }

  @Test
  public void givenPartyDoesNotExist_whenPlayerMoves_thenReturnErrorMessage() throws Exception {
    // Arrange
    Exception exception =
        new PartyNotFoundException("Party not found", new RuntimeException("Cause exception"));
    when(playerMoveUseCase.playerMove(any(), any(), anyInt())).thenThrow(exception);

    // Act & Assert
    mockMvc
        .perform(
            post("/api/v1/party/{partyId}/move", UUID.randomUUID())
                .content("{\"player\": \"PLAYER_ONE\",\"pitIndex\": 0}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(status().reason(exception.getMessage()));
  }

  @Test
  public void givenPartyDoesNotExist_whenWrongPlayerMoves_thenReturnErrorMessage()
      throws Exception {
    // Arrange
    Exception exception = new InvalidPitOwnerException("Invalid pit owner");
    when(playerMoveUseCase.playerMove(any(), any(), anyInt())).thenThrow(exception);

    // Act & Assert
    mockMvc
        .perform(
            post("/api/v1/party/{partyId}/move", UUID.randomUUID())
                .content("{\"player\": \"PLAYER_ONE\",\"pitIndex\": 0}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andExpect(status().reason(exception.getMessage()));
  }
}
