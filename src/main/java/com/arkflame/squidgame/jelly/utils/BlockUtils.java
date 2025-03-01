package com.arkflame.squidgame.jelly.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.arkflame.squidgame.utils.Materials;

public class BlockUtils {
    public static void destroyBlockGroup(Block block, boolean useParticles) {
        Material target = block.getType();
        Block up = block.getRelative(BlockFace.SOUTH);
        Block down = block.getRelative(BlockFace.NORTH);
        Block left = block.getRelative(BlockFace.EAST);
        Block right = block.getRelative(BlockFace.WEST);

        if (useParticles) {
            block.breakNaturally();
        } else {
            block.setType(Materials.get("AIR"));
        }

        if (up.getType() == target) {
            destroyBlockGroup(up, useParticles);
        }

        if (down.getType() == target) {
            destroyBlockGroup(down, useParticles);
        }

        if (left.getType() == target) {
            destroyBlockGroup(left, useParticles);
        }

        if (right.getType() == target) {
            destroyBlockGroup(right, useParticles);
        }
    }

    public static void destroyBlockGroup(Block block) {
        destroyBlockGroup(block, true);
    }
}
