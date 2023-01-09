package com.soyaldo.commandapi;

import com.soyaldo.commandapi.interfaces.PlayerOptionsRequest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class example {

    public static void main(String[] args) {

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
