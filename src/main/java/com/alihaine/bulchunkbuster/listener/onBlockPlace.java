package com.alihaine.bulchunkbuster.listener;

import com.alihaine.bulchunkbuster.ChunkBuster;
import com.alihaine.bulchunkbuster.buster.BlockBuster;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onBlockPlace implements Listener {
    private final BusterConfig busterConfig = ChunkBuster.getBusterConfig();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final Block block = event.getBlock();
        if (!busterConfig.isBlockBuster(block.getType()))
            return;
        if (busterConfig.isBlacklistedWorld(block.getLocation().getWorld())) {
            event.setCancelled(true);
            return;
        }
        if (busterConfig.getDestroyBlockBuster())
            block.setType(Material.AIR);
        new BlockBuster(block.getChunk(), block.getLocation());
    }
}
