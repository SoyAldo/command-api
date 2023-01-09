package com.soyaldo.commandapi;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class example {

    public static void main(String[] args) {

        Command.create("")
                .setBothOptions("help,reload,version")
                .addSubCommand(
                        Command.create("help")
                                .setPlayerExecution(
                                        (player, previousArguments, currentArgument, nextArguments) -> {
                                            player.sendMessage("Help message for players");
                                        }
                                )
                                .setConsoleExecution(
                                        (consoleCommandSender, previousArguments, currentArgument, nextArguments) -> {
                                            consoleCommandSender.sendMessage("Help message for console");
                                        }
                                )
                )
                .addSubCommand(
                        Command.create("reload")
                                .setBothExecution(
                                        (commandSender, previousArguments, currentArgument, nextArguments) -> {
                                            commandSender.sendMessage("Reloaded");
                                        }
                                )
                )
                .addSubCommand(
                        Command.create("version")
                                .setBothExecution(
                                        (commandSender, previousArguments, currentArgument, nextArguments) -> {
                                            commandSender.sendMessage("");
                                        }
                                )
                );

        Command.create("item")
                .addSubCommand(
                        Command.create("edit")
                                .setPlayerOptions("name,lore")
                                .addSubCommand(
                                        Command.create("lore")
                                                .setPlayerOptions("add,edit,set,remove,list")
                                )
                                .setPlayerExecution((player, previousArguments, currentArgument, nextArguments) -> {
                                    player.sendMessage("Invalid input option");
                                })
                )
                .addSubCommand(
                        Command.create("give")
                                .setPlayerOptions("stone,dirt")
                                .addSubCommand(
                                        Command.create("*")
                                                .setPlayerExecution((player, previousArguments, currentArgument, nextArguments) -> {
                                                    ItemStack itemStack = new ItemStack(Material.valueOf(currentArgument));

                                                    if (nextArguments.length > 0) {
                                                        itemStack.setAmount(Integer.parseInt(nextArguments[0]));
                                                    }

                                                    player.getInventory().addItem(itemStack);
                                                })
                                )
                );

    }
}
