package com.huskydreaming.bouncyball.inventories;

import com.huskydreaming.bouncyball.data.particles.ParticleColor;
import com.huskydreaming.bouncyball.data.particles.ParticleData;
import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.pareseables.Menu;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ColorInventory extends InventoryPageProvider<ParticleColor> {

    private final String key;
    private final ParticleService particleService;


    public ColorInventory(HuskyPlugin plugin, String key, int rows, ParticleColor[] array) {
        super(rows, array);
        this.key = key;

        this.particleService = plugin.provide(ParticleService.class);
        this.smartInventory = plugin.provide(InventoryService.class).getParticleInventory(plugin, key);
    }

    @Override
    public ItemStack construct(Player player, int index, ParticleColor particleColor) {
        ParticleData particleData = particleService.getParticle(key);
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
            particleService.getParticle(key).setColor(particleColor.getColor());
            contents.inventory().open(player);
        }
    }
}