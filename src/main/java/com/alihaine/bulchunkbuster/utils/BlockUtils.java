package com.alihaine.bulchunkbuster.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.lang.reflect.Method;

public class BlockUtils {
    private static boolean MODERN;
    private static Method getBlockDataMethod;
    private static Method getMaterialMethod;

    static {
        try {
            // Check if getBlockData() exists (1.13+)
            getBlockDataMethod = Block.class.getMethod("getBlockData");
            // getMaterial() method from BlockData
            Class<?> blockDataClass = Class.forName("org.bukkit.block.data.BlockData");
            getMaterialMethod = blockDataClass.getMethod("getMaterial");
            MODERN = true;
        } catch (Exception ignored) {
            MODERN = false;
        }
    }

    public static Material getMaterial(Block block) {
        if (MODERN) {
            try {
                Object blockData = getBlockDataMethod.invoke(block);
                return (Material) getMaterialMethod.invoke(blockData);
            } catch (Exception e) {
                Bukkit.getLogger().severe("[BulChunkBuster] the plugin can't get the right material, please contact the dev");
                return block.getType();
            }
        } else {
            return block.getType();
        }
    }
}
