package com.huskydreaming.bouncysnowballs.data;

import org.bukkit.block.Block;

import java.util.List;

public record ProjectileData(List<String> blocks, boolean returns, boolean drops, double launchVelocity, double damping, double threshold) {

    public boolean isBouncyBlock(Block block) {
        if(blocks != null && !blocks.isEmpty() && block != null) {
            return blocks.contains(block.getType().name());
        }
        return true;
    }
}
