package com.huskydreaming.bouncyball;

import com.huskydreaming.bouncyball.service.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.storage.Locale;
import com.huskydreaming.bouncyball.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) return sendPlayer(player, args);
        if (sender instanceof ConsoleCommandSender consoleCommandSender) return sendConsole(consoleCommandSender, args);
        return false;
    }

    private boolean sendPlayer(Player player, String[] args) {
        if (player.hasPermission("bouncyball.give")) {
            String string = args[0];
            if (args.length == 1) {
                ItemStack itemStack = projectileService.getItemStackFromKey(string);
                if (itemStack == null) {
                    player.sendMessage(Locale.BOUNCY_BALL_NULL.prefix(string));
                    return true;
                }

                player.getInventory().addItem(itemStack);
                player.sendMessage(Locale.BOUNCY_BALL_GIVE.prefix(string));
            } else if (args.length == 2) {
                String number = args[1];
                ItemStack itemStack = projectileService.getItemStackFromKey(string);
                if (!Util.isNumeric(number)) {
                    player.sendMessage(Locale.INVALID_NUMBER.prefix());
                    return true;
                }

                itemStack.setAmount(Integer.parseInt(number));
                player.getInventory().addItem(itemStack);
                player.sendMessage(Locale.BOUNCY_BALL_GIVE_AMOUNT.prefix(number, string));
            }
        } else {
            player.sendMessage(Locale.NO_PERMISSIONS.prefix());
        }
        return false;
    }


    private boolean sendConsole(ConsoleCommandSender consoleCommandSender, String[] args) {
        if (args.length == 2) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                consoleCommandSender.sendMessage(Locale.PLAYER_NULL.parse());
                return true;
            }
            String string = args[0];
            ItemStack itemStack = projectileService.getItemStackFromKey(string);
            if (itemStack == null) {
                consoleCommandSender.sendMessage(Locale.BOUNCY_BALL_NULL.parse());
                return true;
            }

            player.getInventory().addItem(itemStack);
            player.sendMessage(Locale.BOUNCY_BALL_GIVE.parameterize(string));
            consoleCommandSender.sendMessage(Locale.BOUNCY_BALL_SEND.parameterize(player.getName(), string));
        } else if (args.length == 3) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                consoleCommandSender.sendMessage(Locale.PLAYER_NULL.parse());
                return true;
            }
            String string = args[0];
            String number = args[2];
            ItemStack itemStack = projectileService.getItemStackFromKey(string);
            if (!Util.isNumeric(number)) {
                consoleCommandSender.sendMessage(Locale.INVALID_NUMBER.parse());
                return true;
            }

            itemStack.setAmount(Integer.parseInt(number));
            player.getInventory().addItem(itemStack);
            player.sendMessage(Locale.BOUNCY_BALL_GIVE_AMOUNT.prefix(number, string));
            consoleCommandSender.sendMessage(Locale.BOUNCY_BALL_SEND_AMOUNT.parameterize(player.getName(), number, string));
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) return projectileService.getProjectileDataMap().keySet().stream().toList();
        return null;
    }
}