package com.soyaldo.commandapi.interfaces;

import org.bukkit.entity.Player;

public interface PlayerExecution {

    void onExecute(Player player, String[] previousArguments, String currentArgument, String[] nextArguments);

}