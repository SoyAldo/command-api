package com.soyaldo.commandapitest;

import com.soyaldo.commandapi.Command;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandApiTest extends JavaPlugin {

    @Override
    public void onEnable() {

        Command.create("CommandApiTest")
                .setBothOptions("test,help,reload,version")
                .addSubCommand(
                        Command.create("test")
                                .addSubCommand(
                                        Command.create("level")
                                                .setPlayerExecution(
                                                        (player, previousArguments, currentArgument, nextArguments) -> {
                                                            player.sendMessage("Three Level Command for Players");
                                                        }
                                                )
                                                .setConsoleExecution(
                                                        (consoleCommandSender, previousArguments, currentArgument, nextArguments) -> {
                                                            consoleCommandSender.sendMessage("Three Level Command for Console");
                                                        }
                                                )
                                )
                )
                .addSubCommand(
                        Command.create("help")
                                .setPlayerExecution(
                                        (player, previousArguments, currentArgument, nextArguments) -> {
                                            player.sendMessage("Help message for players");
                                        }
                                )
                                .setConsoleExecution(
                                        (commandSender, previousArguments, currentArgument, nextArguments) -> {
                                            commandSender.sendMessage("Help message for console");
                                        }
                                )
                )
                .addSubCommand(
                        Command.create("reload")
                                .setBothExecution(
                                        (commandSender, previousArguments, currentArgument, nextArguments) -> {
                                            commandSender.sendMessage("Reload message");
                                        }
                                )
                )
                .addSubCommand(
                        Command.create("version")
                                .setBothExecution(
                                        (commandSender, previousArguments, currentArgument, nextArguments) -> {
                                            commandSender.sendMessage("Version message");
                                        }
                                )
                ).registerCommand(this);

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void onReload() {

    }

}