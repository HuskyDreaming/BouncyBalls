package com.huskydreaming.bouncyball.inventories;

import com.huskydreaming.bouncyball.data.particles.ParticleColor;
import com.huskydreaming.bouncyball.data.particles.ParticleData;
import com.huskydreaming.bouncyball.enumerations.Menu;
import com.huskydreaming.bouncyball.repositories.interfaces.ParticleRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.providers.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.builders.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ColorInventory extends InventoryPageProvider<ParticleColor> {

    private final String key;
    private final ParticleRepository particleRepository;


    public ColorInventory(HuskyPlugin plugin, String key, int rows, ParticleColor[] array) {
        super("Colors", rows, array);
        this.key = key;

        this.particleRepository = plugin.provide(ParticleRepository.class);
    }

    @Override
    public ItemStack construct(Player player, int index, ParticleColor particleColor) {
        ParticleData particleData = particleRepository.getParticleData(key);
        return ItemBuilder.create()
                .setDisplayName(Menu.EDIT_COLOR_TITLE.parameterize(particleColor.getChatColor(), particleColor.getChatColor().getName()))
                .setLore(Menu.EDIT_COLOR_LORE.parseList())
                .setEnchanted(particleData.getColor() == particleColor.getColor())
                .setMaterial(particleColor.getMaterial())
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, ParticleColor particleColor, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            ParticleData particleData = particleRepository.getParticleData(key);
            particleData.setColor(particleColor.getColor());
            contents.inventory().open(player);
        }
    }
}