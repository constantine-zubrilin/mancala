package me.zubrilincp.mankala.application.port.in;

import java.util.UUID;
import me.zubrilincp.mankala.domain.model.Party;

public interface ManagePartyUseCase {

  Party createParty();

  Party findParty(UUID partyId);
}
