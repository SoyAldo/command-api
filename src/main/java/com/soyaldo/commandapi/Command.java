package com.soyaldo.commandapi;

import com.soyaldo.commandapi.interfaces.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Command extends org.bukkit.command.Command implements CommandExecutor, TabCompleter {

    private BothOptionsRequest bothOptionsRequest;
    private BothExecution bothExecution;
    private PlayerOptionsRequest playerOptionsRequest;
    private PlayerExecution playerExecution;
    private ConsoleOptionsRequest consoleOptionsRequest;
    private ConsoleExecution consoleExecution;
    private HashMap<String, Command> subCommands = new HashMap<>();

    public Command(String name) {
        super(name);
    }

    public static Command create(String name) {
        return new Command(name);
    }

    public Command setBothOptions(BothOptionsRequest bothOptionsRequest) {
        this.bothOptionsRequest = bothOptionsRequest;
        return this;
    }

    public Command setBothOptions(String bothOptionsRequest) {
        this.playerOptionsRequest = (commandSender, previousArguments, currentArgument, nextArguments) -> bothOptionsRequest;
        return this;
    }

    public Command setBothExecution(BothExecution bothExecution) {
        this.bothExecution = bothExecution;
        return this;
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


    private void run(CommandSender commandSender, String[] previousArguments, String currentArgument, String[] nextArguments) {

        if (bothExecution != null) {
            bothExecution.onExecute(commandSender, previousArguments, currentArgument, nextArguments);
            return;
        }

        if (commandSender instanceof Player) {
            if (playerExecution == null) return;
            playerExecution.onExecute((Player) commandSender, previousArguments, currentArgument, nextArguments);
        } else {
            if (consoleExecution == null) return;
            consoleExecution.onExecute((ConsoleCommandSender) commandSender, previousArguments, currentArgument, nextArguments);
        }

    }

    public void onCommand(CommandSender commandSender, String[] arguments, int index) {

        String[] previousArguments;
        String currentArgument;
        String[] nextArguments;

        switch (index) {
            case -1: {
                previousArguments = new String[] {};
                currentArgument = getName();
                nextArguments = arguments;
                break;
            }
            case 0: {
                previousArguments = new String[] {};
                currentArgument = arguments[index];
                nextArguments = Arrays.copyOfRange(arguments, index + 1, arguments.length);
                break;
            }
            default: {
                previousArguments = Arrays.copyOfRange(arguments, 0, index);
                currentArgument = arguments[index];
                nextArguments = Arrays.copyOfRange(arguments, index + 1, arguments.length);
                break;
            }
        }

        if (currentArgument.equals("")) {
            run(commandSender, previousArguments, currentArgument, nextArguments);
            return;
        }

        if (subCommands.containsKey("*")) {
            Command command = subCommands.get("*");
            command.onCommand(commandSender, arguments, index + 1);
            return;
        }

        if (subCommands.containsKey(currentArgument)) {
            Command command = subCommands.get(currentArgument);
            command.onCommand(commandSender, arguments, index + 1);
        }

    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String commandLabel, String[] args) {
        onCommand(commandSender, args, -1);
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {
        onCommand(commandSender, args, -1);
        return true;
    }

    public String onTabComplete(CommandSender commandSender, String[] args, int index) {
        commandSender.sendMessage("Args:" + String.join(",", args));
        commandSender.sendMessage("Length:" + args.length);
        commandSender.sendMessage("Length Split:" + String.join(",", args).split(",").length);
        commandSender.sendMessage("Index:" + index);
        return "";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        String result = onTabComplete(sender, args, -1);
        if (result.equals("")) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(result.split(","));
        }
    }

    public void registerCommand(JavaPlugin javaPlugin) {
        PluginCommand pluginCommand = javaPlugin.getCommand(getName());
        if (pluginCommand == null) {
            try {
                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
                commandMap.register(getName(), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

}