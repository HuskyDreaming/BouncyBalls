package com.huskydreaming.bouncyball.utilities;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Projectile;
import org.bukkit.util.BlockIterator;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final String regex = "\\s+";

    public static boolean isNumeric(String string) {
        return pattern.matcher(string).matches();
    }

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        return Arrays.stream(input.split(regex))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public static BlockFace getInverseFace(Projectile projectile) {
        Location location = projectile.getLocation();
        World world = location.getWorld();
        if (world == null) return null;

        Block block = location.getBlock();
        BlockIterator blockIterator = new BlockIterator(world, location.toVector(), projectile.getVelocity(), 0.0D, 3);

        Block previousBlock = block;
        Block nextBlock = blockIterator.next();

        while (blockIterator.hasNext() && (nextBlock.getType() == Material.AIR || nextBlock.isLiquid() || nextBlock.equals(block))) {
            previousBlock = nextBlock;
            nextBlock = blockIterator.next();
        }

        BlockFace blockFace = nextBlock.getFace(previousBlock);
        return blockFace == BlockFace.SELF ? BlockFace.UP : blockFace;
    }
}