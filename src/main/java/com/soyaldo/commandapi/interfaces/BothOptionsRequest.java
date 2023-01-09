package com.soyaldo.commandapi.interfaces;

import org.bukkit.command.CommandSender;

public interface BothOptionsRequest {

    String onRequest(CommandSender commandSender, String[] previousArguments, String currentArgument, String[] nextArguments);

}