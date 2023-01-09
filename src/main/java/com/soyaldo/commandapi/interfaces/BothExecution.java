package com.soyaldo.commandapi.interfaces;

import org.bukkit.command.CommandSender;

public interface BothExecution {

    void onExecute(CommandSender commandSender, String[] previousArguments, String currentArgument, String[] nextArguments);

}