package com.soyaldo.commandapi.interfaces;

import org.bukkit.command.ConsoleCommandSender;

public interface ConsoleExecution {

    void onExecute(ConsoleCommandSender consoleCommandSender, String[] previousArguments, String currentArgument, String[] nextArguments);

}