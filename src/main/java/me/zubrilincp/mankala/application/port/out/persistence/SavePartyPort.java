package me.zubrilincp.mankala.application.port.out.persistence;

import me.zubrilincp.mankala.domain.model.Party;

public interface SavePartyPort {

  Party saveParty(Party party);
}
