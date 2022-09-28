package com.huskydreaming.bouncyball.commands;

import com.huskydreaming.bouncyball.service.ProjectileService;
import org.apache.maven.shared.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BouncyBallCommand implements CommandExecutor {

    private final ProjectileService projectileService;

    public BouncyBallCommand(ProjectileService projectileService) {
        this.projectileService = projectileService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player) {
            if (args.length < 1) return true;
            if (args[0].equalsIgnoreCase("give")) {
                if (args.length == 1) {
                    return true;
                }

                String string = args[1];
                ItemStack itemStack = projectileService.getItemStackFromKey(string);
                if (itemStack == null) {
                    sender.sendMessage("The " + string + " bouncy ball does not seem to exist.");
                    return true;
                }

                if (args.length == 2) {


                    player.getInventory().addItem(itemStack);
                    player.sendMessage("You have been given the " + string + " bouncy ball.");
                }

                if (args.length == 3) {
                    if (!StringUtils.isNumeric(args[2])) {
                        sender.sendMessage("You must provide a valid number");
                        return true;
                    }

                    itemStack.setAmount(Integer.parseInt(args[2]));
                    player.getInventory().addItem(itemStack);
                    player.sendMessage("You have been given " + args[2] + " " + string + " bouncy balls.");

                    return true;
                }
            }
        } else {
            sender.sendMessage("You must be a player to run that command.");
        }
        return false;
    }
}
