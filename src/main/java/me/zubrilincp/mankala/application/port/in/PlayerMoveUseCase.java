package me.zubrilincp.mankala.application.port.in;

import java.util.UUID;
import me.zubrilincp.mankala.domain.commons.Player;
import me.zubrilincp.mankala.domain.model.Party;

public interface PlayerMoveUseCase {

  Party playerMove(UUID partyId, Player player, int pitIndex);
}
