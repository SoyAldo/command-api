package com.soyaldo.commandapi;

import com.soyaldo.commandapi.interfaces.ConsoleExecution;
import com.soyaldo.commandapi.interfaces.ConsoleOptionsRequest;
import com.soyaldo.commandapi.interfaces.PlayerExecution;
import com.soyaldo.commandapi.interfaces.PlayerOptionsRequest;
import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Command implements CommandExecutor, TabCompleter {

    @Getter
    private final String name;
    private PlayerOptionsRequest playerOptionsRequest;
    private PlayerExecution playerExecution;
    private ConsoleOptionsRequest consoleOptionsRequest;
    private ConsoleExecution consoleExecution;
    private HashMap<String, Command> subCommands = new HashMap<>();

    public Command(String name) {
        this.name = name;

    }

    public static Command create(String name) {
        return new Command(name);
    }

    public Command setPlayerOptions(PlayerOptionsRequest playerOptionsRequest) {
        this.playerOptionsRequest = playerOptionsRequest;
        return this;
    }

    public Command setPlayerOptions(String playerOptionsRequest) {
        this.playerOptionsRequest = (player, previousArguments, currentArgument, nextArguments) -> playerOptionsRequest;
        return this;
    }

    public Command setPlayerExecution(PlayerExecution playerExecution) {
        this.playerExecution = playerExecution;
        return this;
    }

    public Command setConsoleOptions(ConsoleOptionsRequest consoleOptionsRequest) {
        this.consoleOptionsRequest = consoleOptionsRequest;
        return this;
    }

    public Command setConsoleOptions(String consoleOptionsRequest) {
        this.consoleOptionsRequest = (console, previousArguments, currentArgument, nextArguments) -> consoleOptionsRequest;
        return this;
    }

    public Command addSubCommand(Command command) {
        subCommands.put(command.getName(), command);
        return this;
    }

    public Command setConsoleExecution(ConsoleExecution consoleExecution) {
        this.consoleExecution = consoleExecution;
        return this;
    }

    public void onPlayerCommand(Player player, String[] arguments, int index) {
        if (subCommands.isEmpty()) {
            if (playerExecution == null) return;
            if (arguments.length >= 1) {
                if (index == -1) {
                    playerExecution.onExecute(player, new String[]{}, getName(), Arrays.copyOfRange(arguments, 1, arguments.length));
                } else if (index == 0) {
                    playerExecution.onExecute(
                            player,
                            new String[]{},
                            arguments[index],
                            Arrays.copyOfRange(arguments, index + 1, arguments.length)
                    );
                } else {
                    playerExecution.onExecute(
                            player,
                            Arrays.copyOfRange(arguments, 0, index),
                            arguments[index],
                            Arrays.copyOfRange(arguments, index + 1, arguments.length)
                    );
                }
            } else {
                playerExecution.onExecute(player, new String[]{}, "", new String[]{});
            }
        } else {
            if (arguments.length > 1) {
                Command command;
                if (subCommands.containsKey("*")) {
                    command = subCommands.get("*");
                } else {
                    command = subCommands.get(arguments[0]);
                }
                if (command == null) return;
                command.onPlayerCommand(player, arguments, index + 1);
            }
        }
    }

    public void onConsoleCommand(ConsoleCommandSender consoleCommandSender, String[] arguments, int index) {
        if (subCommands.isEmpty()) {
            if (consoleExecution == null) return;
            if (arguments.length >= 1) {
                if (index == -1) {
                    consoleExecution.onExecute(consoleCommandSender, new String[]{}, getName(), Arrays.copyOfRange(arguments, 1, arguments.length));
                } else if (index == 0) {
                    consoleExecution.onExecute(
                            consoleCommandSender,
                            new String[]{},
                            arguments[index],
                            Arrays.copyOfRange(arguments, index + 1, arguments.length)
                    );
                } else {
                    consoleExecution.onExecute(
                            consoleCommandSender,
                            Arrays.copyOfRange(arguments, 0, index),
                            arguments[index],
                            Arrays.copyOfRange(arguments, index + 1, arguments.length)
                    );
                }
            } else {
                consoleExecution.onExecute(consoleCommandSender, new String[]{}, "", new String[]{});
            }
        } else {
            if (arguments.length > 1) {
                Command command;
                if (subCommands.containsKey("*")) {
                    command = subCommands.get("*");
                } else {
                    command = subCommands.get(arguments[0]);
                }
                if (command == null) return;
                command.onConsoleCommand(consoleCommandSender, arguments, index + 1);
            }
        }
    }

    public void onCommand(CommandSender commandSender, String[] args) {
        if (subCommands.size() == 0) {
            if (commandSender instanceof Player) {
                onPlayerCommand((Player) commandSender, args, -1);
            } else {
                onConsoleCommand((ConsoleCommandSender) commandSender, args, -1);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        onCommand(commandSender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return null;
    }
}