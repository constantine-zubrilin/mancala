package me.zubrilincp.mankala.adapter.config;

import me.zubrilincp.mankala.adapter.out.persistence.PartyPersistenceAdapter;
import me.zubrilincp.mankala.adapter.out.persistence.repository.PartyRepository;
import me.zubrilincp.mankala.application.port.in.ManagePartyUseCase;
import me.zubrilincp.mankala.application.port.in.PlayerMoveUseCase;
import me.zubrilincp.mankala.application.port.out.persistence.LoadPartyPort;
import me.zubrilincp.mankala.application.port.out.persistence.SavePartyPort;
import me.zubrilincp.mankala.application.service.PartyService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({PartyProperties.class})
public class BeanConfiguration {
  @Bean
  SavePartyPort savePartyPort(PartyRepository partyRepository) {
    return new PartyPersistenceAdapter(partyRepository);
  }

  @Bean
  LoadPartyPort loadPartyPort(PartyRepository partyRepository) {
    return new PartyPersistenceAdapter(partyRepository);
  }

  @Bean
  public ManagePartyUseCase managePartyUseCase(
      PartyProperties partyProperties, SavePartyPort savePartyPort, LoadPartyPort loadPartyPort) {
    return new PartyService(partyProperties, savePartyPort, loadPartyPort);
  }

  @Bean
  public PlayerMoveUseCase playerMoveUseCase(
      PartyProperties partyProperties, SavePartyPort savePartyPort, LoadPartyPort loadPartyPort) {
    return new PartyService(partyProperties, savePartyPort, loadPartyPort);
  }
}
