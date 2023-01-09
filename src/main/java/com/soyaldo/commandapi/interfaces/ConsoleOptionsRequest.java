package com.soyaldo.commandapi.interfaces;

import org.bukkit.entity.Player;

public interface ConsoleOptionsRequest {

    String onRequest(Player player, String[] previousArguments, String currentArgument, String[] nextArguments);

}