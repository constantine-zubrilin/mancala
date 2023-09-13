package me.zubrilincp.mankala.party;

import static me.zubrilincp.mankala.util.mother.PartyMother.aParty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.util.UUID;
import me.zubrilincp.mankala.adapter.out.persistence.entity.PartyEntity;
import me.zubrilincp.mankala.adapter.out.persistence.repository.PartyRepository;
import me.zubrilincp.mankala.domain.model.Party;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class PartyApiSystemTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private PartyRepository partyRepository;

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Container
  private static PostgreSQLContainer postgresqlContainer =
      new PostgreSQLContainer("postgres:13.1")
          .withDatabaseName("postgres")
          .withUsername("postgres")
          .withPassword("postgres");

  @DynamicPropertySource
  public static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresqlContainer::getUsername);
    registry.add("spring.datasource.password", postgresqlContainer::getPassword);
  }

  @BeforeEach
  void setUp() {
    partyRepository.deleteAll();
  }

  @Test
  void givenCreatePartyRequest_whenPostRequest_thenCreateParty() throws Exception {
    // Arrange & Act
    MvcResult mvcResult =
        mockMvc
            .perform(post("/api/v1/party"))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

    // Assert
    String response = mvcResult.getResponse().getContentAsString();
    Party responseParty = objectMapper.readValue(response, Party.class);
    Party savedParty =
        partyRepository
            .findById(UUID.fromString(JsonPath.parse(response).read("$.id")))
            .get()
            .toParty();

    assertNotNull(savedParty);
    assertEquals(responseParty, savedParty);
  }

  @Test
  void givenExistingParty_whenFetched_thenReturnParty() throws Exception {
    // Arrange
    PartyEntity existingPartyEntity = partyRepository.save(new PartyEntity(aParty()));

    // Act
    MvcResult mvcResult =
        mockMvc
            .perform(get("/api/v1/party/" + existingPartyEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

    // Assert
    String response = mvcResult.getResponse().getContentAsString();
    Party responseParty = objectMapper.readValue(response, Party.class);
    Party savedParty = existingPartyEntity.toParty();

    assertNotNull(savedParty);
    assertEquals(responseParty, savedParty);
  }

  @Test
  void givenExistingParty_whenPlayerMove_thenReturnNewPartyState() throws Exception {
    // Arrange
    PartyEntity existingPartyEntity = partyRepository.save(new PartyEntity(aParty()));

    // Act
    MvcResult mvcResult =
        mockMvc
            .perform(
                post("/api/v1/party/" + existingPartyEntity.getId() + "/move")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"player\": \"PLAYER_ONE\",\"pitIndex\": 0}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

    // Assert
    String response = mvcResult.getResponse().getContentAsString();
    Party responseParty = objectMapper.readValue(response, Party.class);
    Party savedParty =
        partyRepository
            .findById(UUID.fromString(JsonPath.parse(response).read("$.id")))
            .get()
            .toParty();

    assertNotNull(savedParty);
    assertEquals(responseParty, savedParty);
  }
}
