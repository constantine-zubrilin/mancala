package me.zubrilincp.mankala.adapter.out.persistence;

import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import me.zubrilincp.mankala.adapter.out.persistence.entity.PartyEntity;
import me.zubrilincp.mankala.adapter.out.persistence.repository.PartyRepository;
import me.zubrilincp.mankala.application.port.out.persistence.LoadPartyPort;
import me.zubrilincp.mankala.application.port.out.persistence.SavePartyPort;
import me.zubrilincp.mankala.domain.exception.persistence.FailedToLoadPartyException;
import me.zubrilincp.mankala.domain.exception.persistence.FailedToSavePartyException;
import me.zubrilincp.mankala.domain.exception.persistence.PartyNotFoundException;
import me.zubrilincp.mankala.domain.model.Party;

@AllArgsConstructor
public class PartyPersistenceAdapter implements LoadPartyPort, SavePartyPort {

  private final PartyRepository partyRepository;

  @Override
  public Party loadParty(UUID id) {
    try {
      PartyEntity partyEntity = partyRepository.findById(id).orElseThrow();
      return partyEntity.toParty();
    } catch (NoSuchElementException e) {
      throw new PartyNotFoundException("Party not found", e);
    } catch (Exception e) {
      throw new FailedToLoadPartyException("Failed to load party", e);
    }
  }

  @Override
  public Party saveParty(Party party) {
    try {
      PartyEntity savedPartyEntity = partyRepository.saveAndFlush(new PartyEntity(party));
      return savedPartyEntity.toParty();
    } catch (Exception e) {
      throw new FailedToSavePartyException("Failed to save party exception", e);
    }
  }
}
