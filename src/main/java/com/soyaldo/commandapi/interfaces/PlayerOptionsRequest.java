package com.soyaldo.commandapi.interfaces;

import org.bukkit.entity.Player;

public interface PlayerOptionsRequest {

    String onRequest(Player player, String[] previousArguments, String currentArgument, String[] nextArguments);

}