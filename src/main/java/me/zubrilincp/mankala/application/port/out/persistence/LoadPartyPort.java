package me.zubrilincp.mankala.application.port.out.persistence;

import java.util.UUID;
import me.zubrilincp.mankala.domain.model.Party;

public interface LoadPartyPort {
  Party loadParty(UUID id);
}
