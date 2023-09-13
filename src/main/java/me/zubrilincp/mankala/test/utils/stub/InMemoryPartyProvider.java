package me.zubrilincp.mankala.test.utils.stub;

import java.util.UUID;
import me.zubrilincp.mankala.application.port.out.persistence.LoadPartyPort;
import me.zubrilincp.mankala.application.port.out.persistence.SavePartyPort;
import me.zubrilincp.mankala.domain.model.Party;

// TODO: probaby not needed
public class InMemoryPartyProvider implements LoadPartyPort, SavePartyPort {
  @Override
  public Party loadParty(UUID id) {
    return null;
  }

  @Override
  public Party saveParty(Party party) {
    return null;
  }
}
