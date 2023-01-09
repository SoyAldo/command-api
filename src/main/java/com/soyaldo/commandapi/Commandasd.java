package com.soyaldo.commandapi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commandasd extends org.bukkit.command.Command implements CommandExecutor, TabCompleter {


    public Commandasd(String command) {
        super(command);
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

    public void onPlayerExecute(Player sender, String[] args) {
    }

    public void onConsoleExecute(ConsoleCommandSender sender, String[] args) {
    }

    private void onCommand(CommandSender commandSender, String[] arguments) {
        if (commandSender instanceof Player) {
            onPlayerExecute((Player) commandSender, arguments);
        } else {
            onConsoleExecute((ConsoleCommandSender) commandSender, arguments);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        onCommand(sender, args);
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        onCommand(sender, args);
        return true;
    }


    public String onPlayerTabComplete(Player requester, int position, String[] previousArguments) {
        return "";
    }

    public String onConsoleTabComplete(ConsoleCommandSender requester, int position, String[] previousArguments) {
        return "";
    }

    private List<String> tabComplete(CommandSender commandSender, String[] arguments) {
        String results;
        int position = arguments.length;
        String currentArgument = arguments[position - 1];
        String[] previousArguments = Arrays.copyOf(arguments, position - 1);

        if (commandSender instanceof Player) {
            results = onPlayerTabComplete((Player) commandSender, position, previousArguments);
        } else {
            results = onConsoleTabComplete((ConsoleCommandSender) commandSender, position, previousArguments);
        }

        List<String> finalResult = new ArrayList<>();

        for (String result : results.split(",")) {
            if (result.startsWith(currentArgument)) {
                finalResult.add(result);
            }
        }

        return finalResult;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return tabComplete(sender, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        return tabComplete(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return tabComplete(sender, args);
    }

}