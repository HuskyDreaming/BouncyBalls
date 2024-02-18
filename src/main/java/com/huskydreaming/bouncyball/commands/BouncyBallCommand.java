package com.huskydreaming.bouncyball.commands;

import com.huskydreaming.bouncyball.service.ProjectileService;
import com.huskydreaming.bouncyball.storage.Locale;
import com.huskydreaming.bouncyball.utilities.Remote;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BouncyBallCommand implements CommandExecutor, TabCompleter {

    private final ProjectileService projectileService;

    public BouncyBallCommand(ProjectileService projectileService) {
        this.projectileService = projectileService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if(player.hasPermission("bouncyball.give")) {
                String string = args[0];
                if (args.length == 1) {
                    ItemStack itemStack = projectileService.getItemStackFromKey(string);
                    if (itemStack == null) {
                        sender.sendMessage(Remote.prefix(Locale.BOUNCY_BALL_NULL, string));
                        return true;
                    }

                    player.getInventory().addItem(itemStack);
                    sender.sendMessage(Remote.prefix(Locale.BOUNCY_BALL_GIVE, string));
                } else if (args.length == 2) {
                    String number = args[1];
                    ItemStack itemStack = projectileService.getItemStackFromKey(string);
                    if (!Remote.isNumeric(number)) {
                        sender.sendMessage(Remote.prefix(Locale.INVALID_NUMBER));
                        return true;
                    }

                    itemStack.setAmount(Integer.parseInt(number));
                    player.getInventory().addItem(itemStack);
                    sender.sendMessage(Remote.prefix(Locale.BOUNCY_BALL_GIVE_AMOUNT, number, string));
                }
            } else {
                sender.sendMessage(Remote.prefix(Locale.NO_PERMISSIONS));
            }
        } else {
            sender.sendMessage("You must be a player to run that command.");
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1) return projectileService.getProjectileMap().values().stream().toList();
        return null;
    }
}
