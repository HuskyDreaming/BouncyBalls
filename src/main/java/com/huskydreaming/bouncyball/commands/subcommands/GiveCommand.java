package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.pareseables.Locale;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import com.huskydreaming.huskycore.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@Command(label = "give")
public class GiveCommand implements SubCommand {

    private final ProjectileService projectileService;

    public GiveCommand(HuskyPlugin plugin) {
        this.projectileService = plugin.provide(ProjectileService.class);
    }


    @Override
    public void run(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof Player player) {
            if (strings.length == 3) {
                Player target = Bukkit.getPlayer(strings[1]);
                if (target == null) {
                    player.sendMessage(Locale.PLAYER_OFFLINE.prefix(strings[1]));
                    return;
                }

                if (projectileService.containKey(strings[2])) {
                    ItemStack itemStack = projectileService.getItemStackFromKey(strings[2]);
                    player.getInventory().addItem(itemStack);
                    player.sendMessage(Locale.BOUNCY_BALL_GIVE.prefix(strings[2].toLowerCase()));
                } else {
                    player.sendMessage(Locale.BOUNCY_BALL_NULL.prefix(strings[2]));
                }
            }

            if (strings.length == 4) {
                Player target = Bukkit.getPlayer(strings[1]);
                if (target == null) {
                    player.sendMessage(Locale.PLAYER_OFFLINE.prefix(strings[1]));
                    return;
                }

                if (!Util.isNumeric(strings[3])) {
                    player.sendMessage(Locale.INVALID_NUMBER.prefix(strings[1]));
                    return;
                }

                if (projectileService.containKey(strings[2])) {
                    ItemStack itemStack = projectileService.getItemStackFromKey(strings[2]);
                    itemStack.setAmount(Integer.parseInt(strings[3]));
                    player.getInventory().addItem(itemStack);
                    player.sendMessage(Locale.BOUNCY_BALL_GIVE.prefix(strings[2].toLowerCase()));
                } else {
                    player.sendMessage(Locale.BOUNCY_BALL_NULL.prefix(strings[1]));
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        if (strings.length == 2) return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        if (strings.length == 3) return projectileService.getProjectileDataMap().keySet().stream().toList();
        return List.of();
    }
}