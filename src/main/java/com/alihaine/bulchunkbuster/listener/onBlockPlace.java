package com.alihaine.bulchunkbuster.listener;

import com.alihaine.bulchunkbuster.ChunkBuster;
import com.alihaine.bulchunkbuster.buster.BlockBuster;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onBlockPlace implements Listener {
    private final BusterConfig busterConfig = ChunkBuster.getBusterConfig();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        if (!busterConfig.isBlockBuster(block.getType()))
            return;
        if (!player.hasPermission("bulchunkbuster.use")) {
            player.sendMessage(busterConfig.getMessage("no_permission"));
            event.setCancelled(true);
            return;
        }
        if (busterConfig.isBlacklistedWorld(block.getLocation().getWorld())) {
            player.sendMessage(busterConfig.getMessage("blacklist_world"));
            event.setCancelled(true);
            return;
        }
        if (busterConfig.getDestroyBlockBuster())
            block.setType(Material.AIR);
        player.sendMessage(busterConfig.getMessage("buster_placed"));
        new BlockBuster(block.getChunk(), block.getLocation(), player);
    }
}
