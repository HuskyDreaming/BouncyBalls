package com.huskydreaming.bouncyball.inventories;

import com.huskydreaming.bouncyball.data.particles.ParticleData;
import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.enumerations.Menu;
import com.huskydreaming.bouncyball.repositories.interfaces.ParticleRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.providers.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.builders.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ParticleInventory extends InventoryPageProvider<Particle> {

    private final String key;
    private final HuskyPlugin plugin;
    private final InventoryHandler inventoryHandler;
    private final ParticleRepository particleRepository;

    public ParticleInventory(HuskyPlugin plugin, String key, int rows, Particle[] array) {
        super("Particles", rows, array);
        this.key = key;
        this.plugin = plugin;

        this.inventoryHandler = plugin.provide(InventoryHandler.class);
        this.particleRepository = plugin.provide(ParticleRepository.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryHandler.getEditInventory(plugin, key)));
        contents.set(0, 1, amountItem(contents));
    }

    @Override
    public ItemStack construct(Player player, int index, Particle particle) {
        ParticleData particleData = particleRepository.getParticleData(key);
        boolean isParticle = particleData.getParticle().name().equals(particle.name());

        Menu title = isParticle ? Menu.EDIT_CURRENT_PARTICLE_TITLE : Menu.EDIT_SET_PARTICLE_TITLE;
        Menu lore = isParticle ? Menu.EDIT_CURRENT_PARTICLE_LORE : Menu.EDIT_SET_PARTICLE_LORE;
        Material material = isParticle ? Material.ENDER_EYE : Material.ENDER_PEARL;

        return ItemBuilder.create()
                .setDisplayName(title.parameterize(particle.name()))
                .setLore(lore.parseList())
                .setMaterial(material)
                .setEnchanted(isParticle)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Particle particle, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            ParticleData particleData = particleRepository.getParticleData(key);
            particleData.setParticle(particle);

            int page = contents.pagination().getPage();
            if (particle == Particle.REDSTONE) {
                inventoryHandler.getColorInventory(plugin, key).open(player);
            } else {
                inventoryHandler.getParticleInventory(plugin, key).open(player, page);
            }
        }
    }

    private ClickableItem amountItem(InventoryContents contents) {
        ParticleData particleData = particleRepository.getParticleData(key);
        int count = particleData.getCount();

        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.EDIT_AMOUNT_TITLE.parameterize(count))
                .setLore(Menu.EDIT_AMOUNT_LORE.parameterizeList())
                .setAmount(Math.max(count, 1))
                .setMaterial(Material.BOOK)
                .build();

        return ClickableItem.of(itemStack, e -> {
            if(e.getWhoClicked() instanceof Player player) {
                int page = contents.pagination().getPage();
                if(e.isRightClick()) {
                    if(count > 1) particleData.setCount(count - 1);
                    contents.inventory().open(player, page);
                } else if(e.isLeftClick()) {
                    if(count < 16) particleData.setCount(count+ 1);
                    contents.inventory().open(player, page);
                }
            }
        });
    }
}