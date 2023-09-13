package me.zubrilincp.mankala.adapter.in.web.request;

import me.zubrilincp.mankala.domain.commons.Player;

// TODO: add validation
public record PlayerMoveRequest(Player player, int pitIndex) {}
