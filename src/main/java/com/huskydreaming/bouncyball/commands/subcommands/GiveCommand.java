package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.handlers.interfaces.ProjectileHandler;
import com.huskydreaming.bouncyball.enumerations.Locale;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import com.huskydreaming.huskycore.utilities.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@CommandAnnotation(label = "give")
public class GiveCommand implements PlayerCommandProvider {

    private final ProjectileHandler projectileHandler;
    private final ProjectileRepository projectileRepository;

    public GiveCommand(HuskyPlugin plugin) {
        this.projectileHandler = plugin.provide(ProjectileHandler.class);
        this.projectileRepository = plugin.provide(ProjectileRepository.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length == 3) {
            Player target = Bukkit.getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage(Locale.PLAYER_OFFLINE.prefix(strings[1]));
                return;
            }

            String projectileName = strings[2].toLowerCase();
            if (projectileRepository.hasProjectileData(projectileName)) {
                ItemStack itemStack = projectileHandler.getItemStackFromKey(projectileName);
                player.getInventory().addItem(itemStack);
                player.sendMessage(Locale.BOUNCY_BALL_GIVE.prefix(projectileName.toLowerCase()));
            } else {
                player.sendMessage(Locale.BOUNCY_BALL_NULL.prefix(projectileName));
            }
        }

        if (strings.length == 4) {
            Player target = Bukkit.getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage(Locale.PLAYER_OFFLINE.prefix(strings[1]));
                return;
            }

            if (!NumberUtil.isNumeric(strings[3])) {
                player.sendMessage(Locale.INVALID_NUMBER.prefix(strings[1]));
                return;
            }

            String projectileName = strings[2].toLowerCase();
            if (projectileRepository.hasProjectileData(projectileName)) {
                ItemStack itemStack = projectileHandler.getItemStackFromKey(projectileName);
                itemStack.setAmount(Integer.parseInt(strings[3]));
                player.getInventory().addItem(itemStack);
                player.sendMessage(Locale.BOUNCY_BALL_GIVE.prefix(projectileName.toLowerCase()));
            } else {
                player.sendMessage(Locale.BOUNCY_BALL_NULL.prefix(projectileName));
            }
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length == 2)
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        if (strings.length == 3) return projectileRepository.getProjectileDataMap().keySet().stream().toList();
        return List.of();
    }
}