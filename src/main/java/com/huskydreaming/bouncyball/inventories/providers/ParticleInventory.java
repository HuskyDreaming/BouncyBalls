package com.huskydreaming.bouncyball.inventories.providers;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.data.ParticleData;
import com.huskydreaming.bouncyball.inventories.base.InventoryPageProvider;
import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.storage.Menu;
import com.huskydreaming.bouncyball.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ParticleInventory extends InventoryPageProvider<Particle> {

    private final String key;
    private final BouncyBallPlugin plugin;
    private final InventoryService inventoryService;
    private final ParticleService particleService;

    public ParticleInventory(BouncyBallPlugin plugin, String key, int rows, Particle[] array) {
        super(rows, array);
        this.key = key;
        this.plugin = plugin;

        this.particleService = plugin.provide(ParticleService.class);
        this.inventoryService = plugin.provide(InventoryService.class);
        this.smartInventory = inventoryService.getEditInventory(plugin, key);
    }

    @Override
    public ItemStack construct(Player player, int index, Particle particle) {
        ParticleData particleData = particleService.getParticle(key);
        boolean isParticle = particleData.getParticle().name().equals(particle.name());

        Menu title = isParticle ? Menu.EDIT_CURRENT_PARTICLE_TITLE : Menu.EDIT_SET_PARTICLE_TITLE;
        Menu lore = isParticle ? Menu.EDIT_CURRENT_PARTICLE_LORE : Menu.EDIT_SET_PARTICLE_LORE;

        return ItemBuilder.create()
                .setDisplayName(title.parameterize(particle.name()))
                .setLore(lore.parseList())
                .setMaterial(Material.NETHER_STAR)
                .setEnchanted(isParticle)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Particle particle, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            particleService.getParticle(key).setParticle(particle);
            if (particle == Particle.REDSTONE) {
                inventoryService.getColorInventory(plugin, key).open(player);
            } else {
                smartInventory.open(player);
            }
        }
    }
}